package vn.trinhtung.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import vn.trinhtung.exception.constant.*;
import vn.trinhtung.dto.EmailForgotPasswordDto;
import vn.trinhtung.dto.ForgotPasswordDto;
import vn.trinhtung.dto.LoginRequestDto;
import vn.trinhtung.dto.LoginResponseDto;
import vn.trinhtung.dto.PasswordEditDto;
import vn.trinhtung.dto.RoleDto;
import vn.trinhtung.dto.UserDto;
import vn.trinhtung.entity.ForgotPassword;
import vn.trinhtung.entity.Role;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.DuplicateResourceException;
import vn.trinhtung.exception.PasswordNotMatchException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.PasswordResetRepository;
import vn.trinhtung.repository.UserRepository;
import vn.trinhtung.security.AppUserDetails;
import vn.trinhtung.security.JwtProvider;
import vn.trinhtung.service.RoleService;
import vn.trinhtung.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final JwtProvider jwtProvider;

	private final ModelMapper modelMapper;

	private final PasswordEncoder encoder;

	private final RoleService roleService;

	private final PasswordResetRepository forgotPasswordRepository;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public LoginResponseDto login(LoginRequestDto dto) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				dto.getEmail(), dto.getPassword());

		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		LoginResponseDto response = new LoginResponseDto();
		response.setToken(jwtProvider.generateToken(userDetails.getUsername()));
		response.setFullname(userDetails.getUser().getFullname());
		List<RoleDto> roles = new ArrayList<>();
		userDetails.getUser().getRoles()
				.forEach(role -> roles.add(new RoleDto(role.getId(), role.getName())));
		response.setRoles(roles);

		return response;
	}

	@Override
	public UserDto register(UserDto dto) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new DuplicateResourceException(UserErrorCode.EMAIL_DUPLICATE,
					"Email already exists");
		}

		User user = new User();
		user.setStatus(true);
		user.setEmail(dto.getEmail());
		user.setFullname(dto.getFullname());
		user.setPassword(encoder.encode(dto.getPassword()));

		Role role = new Role();
		role.setId(roleService.getByName("USER").getId());

		user.setRoles(Arrays.asList(role));

		User saved = userRepository.save(user);

		return modelMapper.map(saved, UserDto.class);
	}

	@Override
	public Page<UserDto> getAll(Integer page, String search) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		if (search != null && !search.isBlank()) {
			return userRepository.search(search, pageable)
					.map(user -> modelMapper.map(user, UserDto.class));
		}
		return userRepository.findAll(pageable).map(user -> modelMapper.map(user, UserDto.class));
	}

	@Override
	public void delete(Integer id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException(UserErrorCode.ID_NOT_FOUND, "User not found");
		}
	}

	@Override
	public UserDto update(Integer id, UserDto dto) {
		Optional<User> userByEmail = userRepository.findByEmail(dto.getEmail());
		if (userByEmail.isPresent()) {
			User user = userByEmail.get();
			if (user.getId() != id) {
				throw new DuplicateResourceException(UserErrorCode.EMAIL_DUPLICATE,
						"Email already exists");
			}
		}

		User user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(UserErrorCode.ID_NOT_FOUND, "User not found"));

		user.setEmail(dto.getEmail());
		user.setFullname(dto.getFullname());
		user.setStatus(dto.getStatus());

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(encoder.encode(dto.getPassword()));
		}

		user.getRoles().forEach(role -> {
			if (role.getName().equals("ADMIN")) {
				user.setStatus(true);
			}
		});

		return modelMapper.map(userRepository.save(user), UserDto.class);
	}

	@Override
	public void editPassword(Integer userId, PasswordEditDto dto) {
		User user = userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException(UserErrorCode.ID_NOT_FOUND, "User not found"));

		if (encoder.matches(dto.getPassword(), user.getPassword())) {
			user.setPassword(encoder.encode(dto.getNewPassword()));
			userRepository.save(user);
		} else {
			throw new PasswordNotMatchException(UserErrorCode.PASSWORD_NOT_MATCH,
					"Password not match");
		}

	}

	@Override
	public void forgotPassword(ForgotPasswordDto dto) {
		User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
				() -> new ResourceNotFoundException(UserErrorCode.ID_NOT_FOUND, "User not found"));
		String code = RandomString.make(64);
		String newPassword = RandomString.make(8).toLowerCase();

		Date expire = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
		forgotPasswordRepository.save(new ForgotPassword(code, newPassword, expire, user));

		EmailForgotPasswordDto message = new EmailForgotPasswordDto(dto.getEmail(), newPassword,
				code);
		kafkaTemplate.send("forgot-password", message);
	}

	@Transactional
	@Override
	public void verifyNewPassword(String code) {
		ForgotPassword forgotPassword = forgotPasswordRepository.findById(code).orElseThrow(
				() -> new ResourceNotFoundException(UserErrorCode.PASSWORD_TOKEN_NOT_FOUND,
						"Token not found"));

		if (forgotPassword.getExpire().before(new Date())) {
			throw new ResourceNotFoundException(UserErrorCode.PASSWORD_TOKEN_NOT_FOUND,
					"Token not found");
		}

		User user = forgotPassword.getUser();
		user.setPassword(encoder.encode(forgotPassword.getNewPassword()));
		forgotPasswordRepository.deleteById(code);

		userRepository.save(user);
	}

	@Override
	public UserDto getById(Integer id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(UserErrorCode.ID_NOT_FOUND, "User not found"));

		return modelMapper.map(user, UserDto.class);
	}

}

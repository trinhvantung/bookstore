package vn.trinhtung.service;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.ForgotPasswordDto;
import vn.trinhtung.dto.LoginRequestDto;
import vn.trinhtung.dto.LoginResponseDto;
import vn.trinhtung.dto.PasswordEditDto;
import vn.trinhtung.dto.UserDto;

public interface UserService {
	LoginResponseDto login(LoginRequestDto dto);

	UserDto register(UserDto dto);

	UserDto getById(Integer id);

	Page<UserDto> getAll(Integer page, String search);

	void delete(Integer id);

	UserDto update(Integer id, UserDto user);

	void editPassword(Integer userId, PasswordEditDto dto);

	void forgotPassword(ForgotPasswordDto dto);

	void verifyNewPassword(String code);
}

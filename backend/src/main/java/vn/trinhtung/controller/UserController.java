package vn.trinhtung.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.ForgotPasswordDto;
import vn.trinhtung.dto.LoginRequestDto;
import vn.trinhtung.dto.PasswordEditDto;
import vn.trinhtung.dto.UserDto;
import vn.trinhtung.security.AppUserDetails;
import vn.trinhtung.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {

		return ResponseEntity.ok(userService.login(dto));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto dto) throws BindException {

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(params = { "id" })
	public ResponseEntity<?> getById(@RequestParam Integer id) {

		return ResponseEntity.ok(userService.getById(id));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(userService.getAll(page, search));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UserDto user) {

		return ResponseEntity.ok(userService.update(id, user));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		userService.delete(id);

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/me/edit-password")
	public ResponseEntity<?> editPassword(@RequestBody @Valid PasswordEditDto dto,
			@AuthenticationPrincipal AppUserDetails userDetails) {
		userService.editPassword(userDetails.getUser().getId(), dto);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/me/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDto dto) {
		userService.forgotPassword(dto);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/me/forgot-password/{code}")
	public ResponseEntity<?> verifyNewPassword(@PathVariable String code) {
		userService.verifyNewPassword(code);

		return ResponseEntity.ok().build();
	}

}

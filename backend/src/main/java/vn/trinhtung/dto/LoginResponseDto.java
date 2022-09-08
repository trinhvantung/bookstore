package vn.trinhtung.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
	private String token;
	private String fullname;
	private List<RoleDto> roles;
}

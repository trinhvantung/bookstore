package vn.trinhtung.dto;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEditDto {
	private String password;

	@Length(min = 5, max = 50, message = "Password between 5 and 50 characters in length")
	private String newPassword;
}

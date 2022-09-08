package vn.trinhtung.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Integer id;

	@Email
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Length(min = 5, max = 100, message = "Fullname between 5 and 100 characters in length")
	private String fullname;
	private Boolean status;
	private List<RoleDto> roles = new ArrayList<>();
}

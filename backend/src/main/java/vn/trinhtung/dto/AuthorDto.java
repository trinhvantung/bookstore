package vn.trinhtung.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotBlank(message = "Author name cannot be empty")
	private String name;

	public AuthorDto(Integer id) {
		super();
		this.id = id;
	}

}

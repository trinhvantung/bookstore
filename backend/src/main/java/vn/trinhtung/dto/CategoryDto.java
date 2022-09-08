package vn.trinhtung.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotBlank(message = "Name cannot be empty")
	@Length(min = 5, max = 255, message = "Category name between 5 and 255 characters in length")
	private String name;

	@NotBlank(message = "Slug cannot be empty")
	@Length(min = 5, max = 50, message = "Category name between 5 and 50 characters in length")
	private String slug;

	private CategoryDto parent;

	public CategoryDto(Integer id) {
		super();
		this.id = id;
	}

}

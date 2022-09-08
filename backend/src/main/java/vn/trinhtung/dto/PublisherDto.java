package vn.trinhtung.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotBlank(message = "Author name not be empty")
	private String name;

	public PublisherDto(Integer id) {
		super();
		this.id = id;
	}

}
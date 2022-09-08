package vn.trinhtung.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDto {
	private Integer id;
	private String key;
	private String value;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private BookDto book;
}

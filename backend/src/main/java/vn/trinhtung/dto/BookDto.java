package vn.trinhtung.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(value = Include.NON_EMPTY)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
	private Integer id;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Slug cannot be empty")
	private String slug;

	private String thumbnail;

	private List<BookImageDto> images;

	private Boolean inStock;
	private Boolean status;

	@NotNull(message = "Category cannot be empty")
	private CategoryDto category;

	@NotNull(message = "Price cannot be empty")
	@Min(value = 0, message = "Minumum price is 0")
	private Integer price;

	private Boolean discount;

	@Min(value = 0, message = "Minumum discount price is 0")
	private Integer discountPrice;

	@NotBlank(message = "Description cannot be empty")
	private String description;

	private List<AttributeDto> attributes = new ArrayList<>();

	@NotNull(message = "Total page cannot be empty")
	private Integer totalPages;

	@NotNull(message = "Width cannot be empty")
	private Integer width;

	@NotNull(message = "Height cannot be empty")
	private Integer height;

	@NotNull(message = "Length cannot be empty")
	private Integer length;

	@NotNull(message = "Publisher cannot be empty")
	private PublisherDto publisher;

	@NotNull(message = "Publishing year cannot be empty")
	private String publishingYear;

	@Size(min = 1, message = "Authors cannot be empty")
	private List<AuthorDto> authors = new ArrayList<>();

	public String getThumbnailUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("api/file/").toUriString()
				+ thumbnail;
	}

	public BookDto(Integer id) {
		super();
		this.id = id;
	}

}

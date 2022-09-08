package vn.trinhtung.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
	private Integer id;

	private BookDto book;

	private Integer quantity;
}

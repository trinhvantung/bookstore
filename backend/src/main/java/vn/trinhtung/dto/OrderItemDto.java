package vn.trinhtung.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
	private Integer id;
	private BookDto book;
	private Integer quantity;
	private Integer price;
}

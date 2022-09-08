package vn.trinhtung.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFilter {
	private Integer[] authors;
	private Integer[] publishers;
	private String[] prices;
	private Integer sort = 1;
}

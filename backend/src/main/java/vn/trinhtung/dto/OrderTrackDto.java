package vn.trinhtung.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.trinhtung.entity.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackDto {
	private Integer id;
	private OrderStatus status;
	private String note;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private OrderDto order;

	private Date createdDate;

	public String getCreatedDateFormat() {
		SimpleDateFormat f = new SimpleDateFormat("hh:mm dd/MM/yyyy");
		return f.format(createdDate);
	}
}

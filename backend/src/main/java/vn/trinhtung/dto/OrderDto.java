package vn.trinhtung.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.trinhtung.entity.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	private Integer id;

	private List<OrderItemDto> orderItems = new ArrayList<>();

	private List<OrderTrackDto> orderTracks = new ArrayList<>();

	private UserDto user;

	@NotBlank(message = "Fullname cannot be empty")
	private String fullName;

	@NotBlank(message = "Address cannot be empty")
	private String address;

	@NotBlank(message = "Phone number cannot be empty")
	private String phoneNumber;

	private String note;
	private Date createdDate;
	private Integer totalPrice;
	private OrderStatus status;

	public String getCreatedDateFormat() {
		SimpleDateFormat f = new SimpleDateFormat("hh:mm dd/MM/yyyy");
		return f.format(createdDate);
	}
}

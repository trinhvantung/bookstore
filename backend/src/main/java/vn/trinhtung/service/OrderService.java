package vn.trinhtung.service;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderTrackDto;
import vn.trinhtung.entity.OrderStatus;

public interface OrderService {
	OrderDto create(OrderDto dto, Integer userId);

	Page<OrderDto> getAllByUser(Integer page, Integer userId);

	Page<OrderDto> getAll(Integer page, OrderStatus status, String search, String sort);
	
	OrderTrackDto updateOrderStatus(Integer orderId, OrderTrackDto dto);
	
	OrderDto getById(Integer id);
	
	OrderDto getByIdAndUserId(Integer id, Integer userId);
	
	void delete(Integer id);
}
 
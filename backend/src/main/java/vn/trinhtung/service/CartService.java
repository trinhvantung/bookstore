package vn.trinhtung.service;

import java.util.List;

import vn.trinhtung.dto.CartItemDto;

public interface CartService {
	CartItemDto add(Integer productId, Integer quantity, Integer userId);

	void updateQuantity(Integer id, Integer quantity, Integer userId);

	void delete(Integer id, Integer userId);
	
	List<CartItemDto> getAll(Integer userId);
}

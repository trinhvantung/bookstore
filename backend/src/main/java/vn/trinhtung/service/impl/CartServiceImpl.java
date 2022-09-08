package vn.trinhtung.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.exception.constant.*;
import vn.trinhtung.dto.CartItemDto;
import vn.trinhtung.entity.Book;
import vn.trinhtung.entity.CartItem;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.CartItemRepository;
import vn.trinhtung.service.CartService;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
	private final CartItemRepository cartItemRepository;
	private final ModelMapper modelMapper;

	@Override
	public CartItemDto add(Integer bookId, Integer quantity, Integer userId) {
		Optional<CartItem> optional = cartItemRepository.findByBookIdAndUserId(bookId, userId);
		CartItem cartItem = null;
		if (optional.isPresent()) {
			cartItem = optional.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		} else {
			User user = new User();
			user.setId(userId);

			Book book = new Book();
			book.setId(bookId);

			cartItem = new CartItem(null, user, book, quantity);
		}
		return modelMapper.map(cartItemRepository.save(cartItem), CartItemDto.class);
	}

	@Transactional
	@Override
	public void updateQuantity(Integer id, Integer quantity, Integer userId) {
		if (!cartItemRepository.existsByIdAndUserId(id, userId)) {
			throw new ResourceNotFoundException(CartItemErrorCode.ID_NOT_FOUND,
					"Cart item not found");
		}
		cartItemRepository.updateQuantity(id, quantity, userId);
	}

	@Transactional
	@Override
	public void delete(Integer id, Integer userId) {
		if (!cartItemRepository.existsByIdAndUserId(id, userId)) {
			throw new ResourceNotFoundException(CartItemErrorCode.ID_NOT_FOUND,
					"Cart item not found");
		}
		cartItemRepository.delete(id, userId);
	}

	@Override
	public List<CartItemDto> getAll(Integer userId) {
		return cartItemRepository.findAllByUserId(userId).stream()
				.map(cartItem -> modelMapper.map(cartItem, CartItemDto.class)).toList();
	}

}

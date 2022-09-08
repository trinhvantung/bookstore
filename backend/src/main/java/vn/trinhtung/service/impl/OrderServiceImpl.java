package vn.trinhtung.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.exception.constant.*;
import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderTrackDto;
import vn.trinhtung.entity.CartItem;
import vn.trinhtung.entity.Order;
import vn.trinhtung.entity.OrderItem;
import vn.trinhtung.entity.OrderStatus;
import vn.trinhtung.entity.OrderTrack;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.OrderException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.CartItemRepository;
import vn.trinhtung.repository.OrderRepository;
import vn.trinhtung.repository.OrderTrackRepository;
import vn.trinhtung.service.OrderService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final CartItemRepository cartItemRepository;
	private final ModelMapper modelMapper;
	private final OrderTrackRepository orderTrackRepository;

	@Override
	public OrderDto create(OrderDto dto, Integer userId) {
		Order order = new Order();
		order.setAddress(dto.getAddress());
		order.setFullName(dto.getFullName());
		order.setNote(dto.getNote());
		order.setPhoneNumber(dto.getPhoneNumber());

		User user = new User();
		user.setId(userId);

		order.setUser(user);

		List<CartItem> cartItems = cartItemRepository.findAllByUserId(userId);
		if (cartItems.size() == 0) {
			throw new OrderException(OrderErrorCode.CART_EMPTY, "Cart is empty");
		}

		Integer totalPrice = 0;
		Set<Integer> cartItemIds = new HashSet<>();
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItem.setPrice(cartItem.getBook().getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			order.getOrderItems().add(orderItem);
			cartItemIds.add(cartItem.getId());

			totalPrice += cartItem.getBook().getPrice() * cartItem.getQuantity();
		}

		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setStatus(OrderStatus.NEW);
		orderTrack.setOrder(order);

		order.setTotalPrice(totalPrice);
		order.getOrderTracks().add(orderTrack);
		order.setStatus(OrderStatus.NEW);
		cartItemRepository.deleteAllById(cartItemIds);
		return modelMapper.map(orderRepository.save(order), OrderDto.class);
	}

	@Override
	public Page<OrderDto> getAllByUser(Integer page, Integer userId) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());

		return orderRepository.findAllByUserId(userId, pageable)
				.map(order -> modelMapper.map(order, OrderDto.class));
	}

	@Override
	public Page<OrderDto> getAll(Integer page, OrderStatus status, String search, String sort) {
		String sortBy = sort.split(",")[0];
		String sortDir = sort.split(",")[1];

		Sort s = Sort.by(sortBy);
		Pageable pageable = PageRequest.of(page - 1, 10,
				sortDir.equals("desc") ? s.descending() : s.ascending());
		return orderRepository.filter(status, search, pageable)
				.map(order -> modelMapper.map(order, OrderDto.class));
	}

	@Transactional
	@Override
	public OrderTrackDto updateOrderStatus(Integer orderId, OrderTrackDto dto) {

		if (!orderRepository.existsById(orderId)) {
			throw new ResourceNotFoundException(OrderErrorCode.ID_NOT_FOUND, "Order not found");
		}

		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setNote(dto.getNote());
		orderTrack.setStatus(dto.getStatus());

		Order order = new Order();
		order.setId(orderId);
		orderTrack.setOrder(order);

		orderRepository.updateStatus(orderId, dto.getStatus());
		return modelMapper.map(orderTrackRepository.save(orderTrack), OrderTrackDto.class);
	}

	@Override
	public OrderDto getById(Integer id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(OrderErrorCode.ID_NOT_FOUND,
						"Order not found"));

		return modelMapper.map(order, OrderDto.class);
	}

	@Override
	public OrderDto getByIdAndUserId(Integer id, Integer userId) {
		Order order = orderRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new ResourceNotFoundException(OrderErrorCode.ID_NOT_FOUND,
						"Order not found"));

		return modelMapper.map(order, OrderDto.class);
	}

	@Override
	public void delete(Integer id) {
		if (!orderRepository.existsById(id)) {
			throw new ResourceNotFoundException(OrderErrorCode.ID_NOT_FOUND, "Order not found");
		}
		orderRepository.deleteById(id);
	}

}

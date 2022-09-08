package vn.trinhtung.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderTrackDto;
import vn.trinhtung.entity.OrderStatus;
import vn.trinhtung.security.AppUserDetails;
import vn.trinhtung.service.OrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {
	private final OrderService orderService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(params = "id")
	public ResponseEntity<?> getById(@RequestParam Integer id) {

		return ResponseEntity.ok(orderService.getById(id));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/user")
	public ResponseEntity<?> getById(@RequestParam Integer id,
			@AuthenticationPrincipal AppUserDetails appUserDetails) {

		return ResponseEntity
				.ok(orderService.getByIdAndUserId(id, appUserDetails.getUser().getId()));
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page,
			@AuthenticationPrincipal AppUserDetails appUserDetails) {

		return ResponseEntity.ok(orderService.getAllByUser(page, appUserDetails.getUser().getId()));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<?> getAll(@RequestParam(required = false) OrderStatus status,
			@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "id,desc") String sort) {

		return ResponseEntity.ok(orderService.getAll(page, status, search, sort));
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid OrderDto dto,
			@AuthenticationPrincipal AppUserDetails appUserDetails) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(orderService.create(dto, appUserDetails.getUser().getId()));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,
			@RequestBody @Valid OrderTrackDto dto) {

		return ResponseEntity.ok(orderService.updateOrderStatus(id, dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		orderService.delete(id);
		return ResponseEntity.noContent().build();
	}

}

package vn.trinhtung.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.security.AppUserDetails;
import vn.trinhtung.service.CartService;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
	private final CartService cartService;

	@PostMapping("/{productId}/{quantity}")
	public ResponseEntity<?> add(@PathVariable Integer productId, @PathVariable Integer quantity,
			@AuthenticationPrincipal AppUserDetails appUserDetails) {

		return ResponseEntity
				.ok(cartService.add(productId, quantity, appUserDetails.getUser().getId()));
	}

	@GetMapping
	public ResponseEntity<?> getAll(@AuthenticationPrincipal AppUserDetails appUserDetails) {
		return ResponseEntity.ok(cartService.getAll(appUserDetails.getUser().getId()));
	}

	@PutMapping("/{id}/{quantity}")
	public ResponseEntity<?> update(@PathVariable Integer id, @PathVariable Integer quantity,
			@AuthenticationPrincipal AppUserDetails appUserDetails) {
		cartService.updateQuantity(id, quantity, appUserDetails.getUser().getId());

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,
			@AuthenticationPrincipal AppUserDetails appUserDetails) {
		cartService.delete(id, appUserDetails.getUser().getId());

		return ResponseEntity.noContent().build();
	}

}

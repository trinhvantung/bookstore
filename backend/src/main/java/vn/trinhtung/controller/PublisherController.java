package vn.trinhtung.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import vn.trinhtung.dto.PublisherDto;
import vn.trinhtung.service.PublisherService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/publisher")
public class PublisherController {
	private final PublisherService publisherService;

	@GetMapping(params = "id")
	public ResponseEntity<?> getById(@RequestParam Integer id) {

		return ResponseEntity.ok(publisherService.getById(id));
	}

	@GetMapping
	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok(publisherService.getAll());
	}

	@GetMapping(params = { "page" })
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(required = false) String search) {

		return ResponseEntity.ok(publisherService.getAll(page, search));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PublisherDto dto) {

		return ResponseEntity.ok(publisherService.create(dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id,
			@Valid @RequestBody PublisherDto dto) {

		return ResponseEntity.ok(publisherService.update(id, dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		publisherService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
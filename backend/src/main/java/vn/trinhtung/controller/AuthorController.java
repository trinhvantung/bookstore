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
import vn.trinhtung.dto.AuthorDto;
import vn.trinhtung.service.AuthorService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/author")
public class AuthorController {
	private final AuthorService authorService;

	@GetMapping(params = "id")
	public ResponseEntity<?> getById(@RequestParam Integer id) {

		return ResponseEntity.ok(authorService.getById(id));
	}

	@GetMapping
	public ResponseEntity<?> getAll() {

		return ResponseEntity.ok(authorService.getAll());
	}

	@GetMapping(params = { "page" })
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(required = false) String search) {

		return ResponseEntity.ok(authorService.getAll(page, search));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody AuthorDto dto) {

		return ResponseEntity.ok(authorService.create(dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody AuthorDto dto) {

		return ResponseEntity.ok(authorService.update(id, dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		authorService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}

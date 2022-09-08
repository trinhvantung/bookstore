package vn.trinhtung.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.BookDto;
import vn.trinhtung.dto.BookFilter;
import vn.trinhtung.service.BookService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {
	private final BookService bookService;

	@GetMapping(params = "id")
	public ResponseEntity<?> getById(@RequestParam Integer id) {

		return ResponseEntity.ok(bookService.getById(id));
	}

	@GetMapping("/category/{slug}")
	public ResponseEntity<?> getAllByCategory(@PathVariable String slug,
			@ModelAttribute BookFilter filter, @RequestParam(defaultValue = "1") Integer page) {
		return ResponseEntity.ok(bookService.getAllByCategory(slug, filter, page));
	}

	@GetMapping("/latest")
	public ResponseEntity<?> getLatest() {
		return ResponseEntity.ok(bookService.getLatest());
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam String q, @ModelAttribute BookFilter filter,
			@RequestParam(defaultValue = "1") Integer page) {
		return ResponseEntity.ok(bookService.search(q, filter, page));
	}

	@GetMapping("/{slug}")
	public ResponseEntity<?> getBySlug(@PathVariable String slug) {

		return ResponseEntity.ok(bookService.getBySlug(slug));
	}

	@GetMapping(params = { "page" })
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "1") Integer page) {

		return ResponseEntity.ok(bookService.getAll(page, search, 1));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestPart BookDto dto,
			@RequestPart List<MultipartFile> images, @RequestPart MultipartFile thumbnail) {
		return ResponseEntity.ok(bookService.create(dto, thumbnail, images));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestPart BookDto dto,
			@RequestPart(required = false) List<MultipartFile> images,
			@RequestPart(required = false) MultipartFile thumbnail) {
		return ResponseEntity.ok(bookService.update(id, dto, thumbnail, images));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

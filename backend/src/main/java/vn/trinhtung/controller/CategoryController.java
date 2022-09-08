package vn.trinhtung.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
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
import vn.trinhtung.dto.CategoryDto;
import vn.trinhtung.service.CategoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping(params = { "id" })
	public ResponseEntity<?> getById(@RequestParam Integer id) {
		return ResponseEntity.ok(categoryService.getById(id));
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(categoryService.getAll());
	}
	
	@GetMapping("/parent")
	public ResponseEntity<?> getAllgetAllParent() {
		return ResponseEntity.ok(categoryService.getAllParent());
	} 

	@GetMapping(params = { "page" })
	public ResponseEntity<?> getAll(@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "1") Integer page) {
		return ResponseEntity.ok(categoryService.getAll(page, search));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid CategoryDto dto) {

		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid CategoryDto dto) {

		return ResponseEntity.ok(categoryService.update(id, dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}

}

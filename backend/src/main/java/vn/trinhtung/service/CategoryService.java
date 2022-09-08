package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.CategoryDto;

public interface CategoryService {
	CategoryDto create(CategoryDto dto);

	CategoryDto update(Integer id, CategoryDto dto);

	void delete(Integer id);

	List<CategoryDto> getAll();
	
	List<CategoryDto> getAllParent();

	Page<CategoryDto> getAll(Integer page, String search);

	CategoryDto getById(Integer id);
}

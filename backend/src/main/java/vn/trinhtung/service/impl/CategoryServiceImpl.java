package vn.trinhtung.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.exception.constant.*;
import vn.trinhtung.dto.CategoryDto;
import vn.trinhtung.entity.Category;
import vn.trinhtung.exception.InvalidParameterException;
import vn.trinhtung.exception.DuplicateResourceException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.CategoryRepository;
import vn.trinhtung.service.CategoryService;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;

	@Caching(
			evict = { @CacheEvict(value = "categories", allEntries = true) }, put = {
					@CachePut(value = "category", key = "#result.id") }
	)
	@Override
	public CategoryDto create(CategoryDto dto) {
		Optional<Category> categoryByName = categoryRepository.findByName(dto.getName());
		if (categoryByName.isPresent()) {
			throw new DuplicateResourceException(CategoryErrorCode.NAME_DUPLICATE,
					"Name already exists");
		}

		Optional<Category> categoryBySlug = categoryRepository.findBySlug(dto.getSlug());
		if (categoryBySlug.isPresent()) {
			throw new DuplicateResourceException(CategoryErrorCode.SLUG_DUPLICATE,
					"Slug already exists");
		}

		Category category = new Category();
		category.setName(dto.getName());
		category.setSlug(dto.getSlug());

		CategoryDto parentDto = dto.getParent();
		if (parentDto == null) {
			category.setParent(null);
		} else {
			Category c = categoryRepository.findById(parentDto.getId()).orElseThrow(
					() -> new ResourceNotFoundException(CategoryErrorCode.PARENT_ID_NOT_FOUND,
							"Parent category not found"));
			if (c.getParent() != null) {
				throw new InvalidParameterException(CategoryErrorCode.PARENT_ID_INVALID,
						"Maximum category is 2 levels");
			}
			category.setParent(new Category(parentDto.getId()));
		}

		return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
	}

	@Caching(
			evict = { @CacheEvict(value = "categories", allEntries = true) }, put = {
					@CachePut(value = "category", key = "#result.id") }
	)
	@Override
	public CategoryDto update(Integer id, CategoryDto dto) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryErrorCode.ID_NOT_FOUND,
						"Category not found"));

		Optional<Category> categoryByName = categoryRepository.findByName(dto.getName());
		if (categoryByName.isPresent() && categoryByName.get().getId() != id) {
			throw new DuplicateResourceException(CategoryErrorCode.NAME_DUPLICATE,
					"Name already exists");
		}

		Optional<Category> categoryBySlug = categoryRepository.findBySlug(dto.getSlug());
		if (categoryBySlug.isPresent() && categoryBySlug.get().getId() != id) {
			throw new DuplicateResourceException(CategoryErrorCode.SLUG_DUPLICATE,
					"Slug already exists");
		}

		category.setName(dto.getName());
		category.setSlug(dto.getSlug());

		CategoryDto parentDto = dto.getParent();
		if (parentDto == null) {
			category.setParent(null);
		} else if (dto.getId() != parentDto.getId()) {
			Category c = categoryRepository.findById(parentDto.getId()).orElseThrow(
					() -> new ResourceNotFoundException(CategoryErrorCode.PARENT_ID_NOT_FOUND,
							"Parent category not found"));
			if (c.getParent() != null) {
				throw new InvalidParameterException(CategoryErrorCode.PARENT_ID_INVALID,
						"Maximum category is 2 levels");
			}
			category.setParent(new Category(parentDto.getId()));
		} else {
			throw new InvalidParameterException(CategoryErrorCode.PARENT_ID_INVALID,
					"Invalid Category Parent");
		}

		return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
	}

	@Caching(
			evict = { @CacheEvict(value = "categories"),
					@CacheEvict(value = "category", key = "#id") }
	)
	@Override
	public void delete(Integer id) {
		if (categoryRepository.existsById(id)) {
			categoryRepository.deleteById(id);
		} else {
			throw new ResourceNotFoundException(CategoryErrorCode.ID_NOT_FOUND,
					"Category not found");
		}
	}

	@Cacheable(value = "categories")
	@Override
	public List<CategoryDto> getAll() {
		return categoryRepository.findAll().stream().map(
				category -> modelMapper.map(categoryRepository.save(category), CategoryDto.class))
				.toList();
	}

	@Override
	public Page<CategoryDto> getAll(Integer page, String search) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		if (search != null && !search.isBlank()) {
			return categoryRepository.search(search, pageable).map(category -> modelMapper
					.map(categoryRepository.save(category), CategoryDto.class));
		}

		return categoryRepository.findAll(pageable).map(
				category -> modelMapper.map(categoryRepository.save(category), CategoryDto.class));
	}

	@Cacheable(value = "category", key = "#id")
	@Override
	public CategoryDto getById(Integer id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CategoryErrorCode.ID_NOT_FOUND,
						"Category not found"));
		return modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllParent() {
		return categoryRepository.findAllByParentIdNull().stream()
				.map(category -> modelMapper.map(category, CategoryDto.class)).toList();
	}

}

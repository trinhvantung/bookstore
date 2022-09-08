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
import vn.trinhtung.dto.AuthorDto;
import vn.trinhtung.entity.Author;
import vn.trinhtung.exception.DuplicateResourceException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.AuthorRepository;
import vn.trinhtung.service.AuthorService;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
	private final AuthorRepository authorRepository;
	private final ModelMapper modelMapper;

	@Caching(
			put = { @CachePut(value = "author", key = "#result.id") }, evict = {
					@CacheEvict(value = "authors", allEntries = true) }
	)
	@Override
	public AuthorDto create(AuthorDto dto) {
		if (authorRepository.findByName(dto.getName()).isPresent()) {
			throw new DuplicateResourceException(AuthorErrorCode.NAME_DUPLICATE,
					"Author name already exists");
		}
		Author author = new Author();
		author.setName(dto.getName());

		Author saved = authorRepository.save(author);
		return modelMapper.map(saved, AuthorDto.class);
	}

	@Caching(
			put = { @CachePut(value = "author", key = "#id") }, evict = {
					@CacheEvict(value = "author", key = "#id"), @CacheEvict(value = "authors") }
	)
	@Override
	public AuthorDto update(Integer id, AuthorDto dto) {
		Optional<Author> authorByName = authorRepository.findByName(dto.getName());

		Author author = authorRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(AuthorErrorCode.ID_NOT_FOUND, "Author not found"));

		if (authorByName.isPresent() && authorByName.get().getId() != id) {
			throw new DuplicateResourceException(AuthorErrorCode.NAME_DUPLICATE,
					"Author name already exists");
		}

		author.setName(dto.getName());

		Author saved = authorRepository.save(author);
		return modelMapper.map(saved, AuthorDto.class);
	}

	@Caching(evict = { @CacheEvict(value = "author", key = "#id"), @CacheEvict(value = "authors") })
	@Override
	public void delete(Integer id) {
		if (!authorRepository.existsById(id)) {
			throw new ResourceNotFoundException(AuthorErrorCode.ID_NOT_FOUND, "Author not found");
		}
		authorRepository.deleteById(id);
	}

	@Cacheable(value = "author", key = "#id")
	@Override
	public AuthorDto getById(Integer id) {
		return authorRepository.findById(id).map(author -> modelMapper.map(author, AuthorDto.class))
				.orElseThrow(() -> new ResourceNotFoundException(AuthorErrorCode.ID_NOT_FOUND,
						"Author not found"));
	}

	@Override
	public Page<AuthorDto> getAll(Integer page, String search) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
		if (search != null && !search.isBlank()) {
			return authorRepository.search(search, pageable)
					.map(author -> modelMapper.map(author, AuthorDto.class));
		}
		return authorRepository.findAll(pageable)
				.map(author -> modelMapper.map(author, AuthorDto.class));
	}

	@Cacheable(value = "authors")
	@Override
	public List<AuthorDto> getAll() {
		// TODO Auto-generated method stub
		return authorRepository.findAll().stream()
				.map(author -> modelMapper.map(author, AuthorDto.class)).toList();
	}

}

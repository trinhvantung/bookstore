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
import vn.trinhtung.dto.PublisherDto;
import vn.trinhtung.entity.Publisher;
import vn.trinhtung.exception.DuplicateResourceException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.PublisherRepository;
import vn.trinhtung.service.PublisherService;

@RequiredArgsConstructor
@Service
public class PublisherServiceImpl implements PublisherService {
	private final PublisherRepository publisherRepository;
	private final ModelMapper modelMapper;

	@Caching(
			put = { @CachePut(value = "publisher", key = "#result.id") }, evict = {
					@CacheEvict(value = "publishers", allEntries = true) }
	)
	@Override
	public PublisherDto create(PublisherDto dto) {
		if (publisherRepository.findByName(dto.getName()).isPresent()) {
			throw new DuplicateResourceException(PublisherErrorCode.NAME_DUPLICATE,
					"Publisher name already exists");
		}
		Publisher publisher = new Publisher();
		publisher.setName(dto.getName());

		Publisher saved = publisherRepository.save(publisher);
		return modelMapper.map(saved, PublisherDto.class);
	}

	@Caching(
			put = { @CachePut(value = "publisher", key = "#id") }, evict = {
					@CacheEvict(value = "publisher", key = "#id"),
					@CacheEvict(value = "publishers") }
	)
	@Override
	public PublisherDto update(Integer id, PublisherDto dto) {
		Optional<Publisher> authorByName = publisherRepository.findByName(dto.getName());

		Publisher publisher = publisherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(PublisherErrorCode.ID_NOT_FOUND,
						"Publisher not found"));

		if (authorByName.isPresent() && authorByName.get().getId() != id) {
			throw new DuplicateResourceException(PublisherErrorCode.NAME_DUPLICATE,
					"Publisher name already exists");
		}

		publisher.setName(dto.getName());

		Publisher saved = publisherRepository.save(publisher);
		return modelMapper.map(saved, PublisherDto.class);
	}

	@Caching(
			evict = { @CacheEvict(value = "publisher", key = "#id"),
					@CacheEvict(value = "publishers") }
	)
	@Override
	public void delete(Integer id) {
		if (!publisherRepository.existsById(id)) {
			throw new ResourceNotFoundException(PublisherErrorCode.ID_NOT_FOUND,
					"Publisher not found");
		}
		publisherRepository.deleteById(id);
	}

	@Cacheable(value = "publisher", key = "#id")
	@Override
	public PublisherDto getById(Integer id) {
		return publisherRepository.findById(id)
				.map(publisher -> modelMapper.map(publisher, PublisherDto.class))
				.orElseThrow(() -> new ResourceNotFoundException(PublisherErrorCode.ID_NOT_FOUND,
						"Publisher not found"));
	}

	@Override
	public Page<PublisherDto> getAll(Integer page, String search) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
		if (search != null && !search.isBlank()) {
			return publisherRepository.search(search, pageable)
					.map(publisher -> modelMapper.map(publisher, PublisherDto.class));
		}
		return publisherRepository.findAll(pageable)
				.map(publisher -> modelMapper.map(publisher, PublisherDto.class));
	}

	@Cacheable(value = "publishers")
	@Override
	public List<PublisherDto> getAll() {
		// TODO Auto-generated method stub
		return publisherRepository.findAll().stream()
				.map(publisher -> modelMapper.map(publisher, PublisherDto.class)).toList();
	}

}

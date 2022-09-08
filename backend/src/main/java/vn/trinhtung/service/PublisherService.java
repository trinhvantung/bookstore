package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.PublisherDto;

public interface PublisherService {
	PublisherDto create(PublisherDto dto);

	PublisherDto update(Integer id, PublisherDto dto);

	void delete(Integer id);

	PublisherDto getById(Integer id);
	
	Page<PublisherDto> getAll(Integer page, String search);
	
	List<PublisherDto> getAll();
}

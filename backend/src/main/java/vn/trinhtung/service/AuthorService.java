package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.AuthorDto;

public interface AuthorService {
	AuthorDto create(AuthorDto dto);

	AuthorDto update(Integer id, AuthorDto dto);

	void delete(Integer id);

	AuthorDto getById(Integer id);
	
	Page<AuthorDto> getAll(Integer page, String search);
	
	List<AuthorDto> getAll();
}

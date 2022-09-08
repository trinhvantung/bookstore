package vn.trinhtung.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.trinhtung.dto.BookDto;
import vn.trinhtung.dto.BookFilter;

public interface BookService {
	BookDto create(BookDto dto, MultipartFile thumbnail, List<MultipartFile> images);

	BookDto update(Integer id, BookDto dto, MultipartFile thumbnail, List<MultipartFile> images);

	BookDto getById(Integer id);

	Page<BookDto> getAll(Integer page, String search, Integer sort);

	BookDto getBySlug(String slug);

	void delete(Integer id);

	Page<BookDto> getAllByCategory(String slug, BookFilter bookFilter, Integer page);

	Page<BookDto> search(String search, BookFilter bookFilter, Integer page);
	
	List<BookDto> getLatest();
}

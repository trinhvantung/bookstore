package vn.trinhtung.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.exception.constant.*;
import vn.trinhtung.dto.BookDto;
import vn.trinhtung.dto.BookFilter;
import vn.trinhtung.entity.Attribute;
import vn.trinhtung.entity.Author;
import vn.trinhtung.entity.Book;
import vn.trinhtung.entity.BookImage;
import vn.trinhtung.entity.Category;
import vn.trinhtung.entity.Publisher;
import vn.trinhtung.exception.InvalidParameterException;
import vn.trinhtung.exception.DuplicateResourceException;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.AuthorRepository;
import vn.trinhtung.repository.BookRepository;
import vn.trinhtung.repository.CategoryRepository;
import vn.trinhtung.repository.PublisherRepository;
import vn.trinhtung.service.BookService;
import vn.trinhtung.util.FileUtil;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;
	private final ModelMapper modelMapper;
	private final PublisherRepository publisherRepository;
	private final CategoryRepository categoryRepository;
	private final AuthorRepository authorRepository;
	private final FileUtil fileUtil;

	@Override
	public BookDto create(BookDto dto, MultipartFile thumbnail, List<MultipartFile> images) {
		if (bookRepository.existsByName(dto.getName())) {
			throw new DuplicateResourceException(BookErrorCode.NAME_DUPLICATE,
					"Book name already exists");
		}
		if (bookRepository.existsBySlug(dto.getSlug())) {
			throw new DuplicateResourceException(BookErrorCode.SLUG_DUPLICATE,
					"Book slug already exists");
		}
		if (!publisherRepository.existsById(dto.getPublisher().getId())) {
			throw new ResourceNotFoundException(PublisherErrorCode.ID_NOT_FOUND,
					"Publisher not found");
		}
		if (!categoryRepository.existsById(dto.getCategory().getId())) {
			throw new ResourceNotFoundException(CategoryErrorCode.ID_NOT_FOUND,
					"Category not found");
		}
		if (images == null || images.size() == 0) {
			throw new InvalidParameterException(BookErrorCode.IMAGES_EMPTY,
					"Images cannot be empty");
		}
		List<BookImage> bookImages = new ArrayList<>();
		Book book = modelMapper.map(dto, Book.class);

		book.getAttributes().forEach(a -> {
			a.setBook(book);
		});

		for (MultipartFile image : images) {
			if (image != null && !image.getOriginalFilename().isBlank()) {
				BookImage bookImage = new BookImage();
				bookImage.setBook(book);
				bookImage.setPath(fileUtil.upload(image, "image"));
				bookImages.add(bookImage);
			}
		}
		book.setImages(bookImages);
		book.setThumbnail(fileUtil.upload(thumbnail, "image"));

		Set<Integer> authorIds = new HashSet<>();
		dto.getAuthors().forEach(author -> authorIds.add(author.getId()));
		if (!authorRepository.existsAllByIdIn(authorIds)) {
			throw new ResourceNotFoundException(1100, "Author not found");
		}
		Book saved = bookRepository.save(book);
		return modelMapper.map(saved, BookDto.class);
	}

	@Override
	public BookDto update(Integer id, BookDto dto, MultipartFile thumbnail,
			List<MultipartFile> images) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(BookErrorCode.NAME_DUPLICATE,
						"Book not found"));

		Optional<Book> bookByName = bookRepository.findByName(dto.getName());
		if (bookByName.isPresent() && bookByName.get().getId() != id) {
			throw new DuplicateResourceException(BookErrorCode.NAME_DUPLICATE,
					"Book name already exists");
		}
		Optional<Book> bookBySlug = bookRepository.findByName(dto.getName());
		if (bookBySlug.isPresent() && bookBySlug.get().getId() != id) {
			throw new DuplicateResourceException(BookErrorCode.SLUG_DUPLICATE,
					"Book slug already exists");
		}
		if (!publisherRepository.existsById(dto.getPublisher().getId())) {
			throw new ResourceNotFoundException(PublisherErrorCode.ID_NOT_FOUND,
					"Publisher not found");
		}
		if (!categoryRepository.existsById(dto.getCategory().getId())) {
			throw new ResourceNotFoundException(CategoryErrorCode.ID_NOT_FOUND,
					"Category not found");
		}
		Set<Integer> authorIds = new HashSet<>();
		dto.getAuthors().forEach(author -> authorIds.add(author.getId()));
		if (!authorRepository.existsAllByIdIn(authorIds)) {
			throw new ResourceNotFoundException(AuthorErrorCode.ID_NOT_FOUND, "Author not found");
		}
		if ((images == null || images.size() == 0)
				&& (dto.getImages() == null || dto.getImages().isEmpty())) {
			throw new InvalidParameterException(BookErrorCode.IMAGES_EMPTY,
					"Images cannot be empty");
		}

		if ((thumbnail == null || thumbnail.getOriginalFilename().isBlank())
				&& (dto.getThumbnail() == null || dto.getThumbnail().isBlank())) {
			throw new InvalidParameterException(BookErrorCode.THUMBNAIL_EMPTY,
					"Thumbnail cannot be empty");
		}
		book.setName(dto.getName());
		book.setSlug(dto.getSlug());
		book.setCategory(modelMapper.map(dto.getCategory(), Category.class));

		book.getAttributes().clear();
		book.getAttributes().addAll(dto.getAttributes().stream().map(a -> {
			Attribute attr = modelMapper.map(a, Attribute.class);
			attr.setBook(book);
			return attr;
		}).collect(Collectors.toList()));
		book.setDescription(dto.getDescription());
		book.setDiscount(dto.getDiscount());
		book.setDiscountPrice(dto.getDiscountPrice());
		book.setHeight(dto.getHeight());
		book.setAuthors(dto.getAuthors().stream().map(a -> modelMapper.map(a, Author.class))
				.collect(Collectors.toList()));

//		book.getImages().clear();
//		book.getImages().addAll(dto.getImages().stream()
//				.map(a -> modelMapper.map(a, BookImage.class)).collect(Collectors.toList()));
		book.setInStock(dto.getInStock());
		book.setLength(dto.getLength());
		book.setName(dto.getName());
		book.setPrice(dto.getPrice());
		book.setPublisher(modelMapper.map(dto.getPublisher(), Publisher.class));
		book.setPublishingYear(dto.getPublishingYear());
		book.setSlug(dto.getSlug());
		book.setTotalPages(dto.getTotalPages());
		book.setWidth(dto.getWidth());
		book.setStatus(dto.getStatus());

		if (thumbnail != null && !thumbnail.getOriginalFilename().isBlank()) {
			System.out.println(book.getThumbnail());
			fileUtil.delete(book.getThumbnail());
			book.setThumbnail(fileUtil.upload(thumbnail, "image"));
		} else {
			book.setThumbnail(dto.getThumbnail());
		}

		List<BookImage> bookImages = dto.getImages().stream()
				.map(a -> modelMapper.map(a, BookImage.class)).collect(Collectors.toList());

		if (images != null && images.size() > 0) {
			for (MultipartFile image : images) {
				if (image != null && !image.getOriginalFilename().isBlank()) {
					BookImage bookImage = new BookImage();
					bookImage.setBook(book);
					bookImage.setPath(fileUtil.upload(image, "image"));
					bookImages.add(bookImage);
				}
			}
		}

//		if (images != null && images.size() > 0) {
//			for (MultipartFile image : images) {
//				if (image != null && !image.getOriginalFilename().isBlank()) {
//					BookImage bookImage = new BookImage();
//					bookImage.setBook(book);
//					bookImage.setPath(fileUtil.upload(image, "image"));
//					book.getImages().add(bookImage);
//				}
//			}
//		}

		Set<Integer> imageIds = dto.getImages().stream().map(i -> i.getId())
				.collect(Collectors.toSet());
		for (BookImage image : book.getImages()) {
			if (!imageIds.contains(image.getId())) {
				fileUtil.delete(image.getPath());
			}
		}

		book.getImages().clear();
		book.getImages().addAll(bookImages);

		System.out.println(book.getImages());

		return modelMapper.map(bookRepository.save(book), BookDto.class);
	}

	@Override
	public BookDto getById(Integer id) {
		Book book = bookRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(BookErrorCode.ID_NOT_FOUND, "Book not found"));

		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public Page<BookDto> getAll(Integer page, String search, Integer sort) {
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
		return bookRepository.findAll(pageable).map(book -> modelMapper.map(book, BookDto.class));
	}

	@Override
	public BookDto getBySlug(String slug) {
		Book book = bookRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException(BookErrorCode.SLUG_DUPLICATE,
						"Book not found"));

		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public void delete(Integer id) {
		Book book = bookRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(BookErrorCode.ID_NOT_FOUND, "Book not found"));
		fileUtil.delete(book.getThumbnail());
		book.getImages().forEach(i -> {
			fileUtil.delete(i.getPath());
		});
		bookRepository.deleteById(id);
	}

	@Override
	public Page<BookDto> getAllByCategory(String slug, BookFilter bookFilter, Integer page) {
		Pageable pageable = PageRequest.of(page - 1, 12);
		List<Integer> ids = categoryRepository.findAllCategoryIdByParent(slug);
		return bookRepository.findAllByCategory(ids, bookFilter, pageable)
				.map(book -> modelMapper.map(book, BookDto.class));
	}

	@Override
	public Page<BookDto> search(String search, BookFilter bookFilter, Integer page) {
		Pageable pageable = PageRequest.of(page - 1, 12);
		return bookRepository.search(search, bookFilter, pageable)
				.map(book -> modelMapper.map(book, BookDto.class));
	}

	@Override
	public List<BookDto> getLatest() {
		Pageable pageable = PageRequest.of(0, 12, Sort.by("id").descending());
		return bookRepository.findAll(pageable).map(book -> modelMapper.map(book, BookDto.class))
				.toList();
	}

}

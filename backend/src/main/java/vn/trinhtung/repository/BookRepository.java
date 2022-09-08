package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.dto.BookFilter;
import vn.trinhtung.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	Optional<Book> findByName(String name);

	Optional<Book> findBySlug(String slug);

	boolean existsByName(String name);

	boolean existsBySlug(String slug);

	Page<Book> findAllByCategory(List<Integer> ids, BookFilter bookFilter, Pageable pageable);

	Page<Book> search(String search, BookFilter bookFilter, Pageable pageable);
}

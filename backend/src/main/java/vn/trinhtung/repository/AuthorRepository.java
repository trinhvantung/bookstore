package vn.trinhtung.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
	Optional<Author> findByName(String name);

	@Query(
		"SELECT a FROM Author a WHERE LOWER(CONCAT(a.id, ' ', a.name)) "
				+ "LIKE LOWER(CONCAT('%', CONCAT(?1, '%')))"
	)
	Page<Author> search(String search, Pageable pageable);

	boolean existsAllByIdIn(Set<Integer> ids);
}

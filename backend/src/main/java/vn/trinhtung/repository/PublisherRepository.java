package vn.trinhtung.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
	Optional<Publisher> findByName(String name);

	@Query(
		"SELECT p FROM Publisher p WHERE LOWER(CONCAT(p.id, ' ', p.name)) "
				+ "LIKE LOWER(CONCAT('%', CONCAT(?1, '%')))"
	)
	Page<Publisher> search(String search, Pageable pageable);
}

package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query(
		"SELECT c FROM Category c WHERE LOWER(CONCAT(c.id, ' ', c.name, ' ', c.slug)) "
				+ "LIKE LOWER(CONCAT('%', CONCAT(?1, '%')))"
	)
	Page<Category> search(String search, Pageable pageable);

	Optional<Category> findByName(String name);

	Optional<Category> findBySlug(String slug);

	List<Category> findAllByParentIdNull();

	boolean existsByParentId(Integer parentId);

	boolean existsById(Integer id);

	@Query(
			value = "select p.id from category p where p.slug = ?1 union "
					+ "select c.id from category c join category c1 on c.parent_id = c1.id "
					+ "where c1.slug =  ?1", nativeQuery = true
	)
	List<Integer> findAllCategoryIdByParent(String slug);
}

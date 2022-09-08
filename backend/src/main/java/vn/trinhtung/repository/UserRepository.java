package vn.trinhtung.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	@Query(
		"SELECT u FROM User u WHERE LOWER(CONCAT(id, ' ', email, ' ', fullname)) "
		+ "LIKE LOWER(CONCAT('%', CONCAT(?1, '%')))"
	)
	Page<User> search(String search, Pageable pageable);

	@Query("UPDATE User u SET u.status = ?1 WHERE u.id = ?2")
	@Modifying
	void updateStatus(Boolean status, Integer id);
	
	boolean existsByEmail(String email);
}

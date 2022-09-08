package vn.trinhtung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	List<CartItem> findAllByUserId(Integer userId);

	Optional<CartItem> findByBookIdAndUserId(Integer bookId, Integer userId);

	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?2 WHERE c.id = ?1 AND c.user.id = ?3")
	void updateQuantity(Integer id, Integer quantity, Integer userId);

	@Query("DELETE CartItem c WHERE c.user.id = ?2 AND c.id = ?1")
	@Modifying
	void delete(Integer id, Integer userId);

	boolean existsByIdAndUserId(Integer id, Integer userId);

	void deleteAllByUserId(Integer userId);
}

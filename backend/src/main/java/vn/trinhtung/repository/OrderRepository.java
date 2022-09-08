package vn.trinhtung.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.Order;
import vn.trinhtung.entity.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	Page<Order> findAllByUserId(Integer userId, Pageable pageable);

	Page<Order> filter(OrderStatus orderStatus, String search, Pageable pageable);

	@Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
	@Modifying
	void updateStatus(Integer id, OrderStatus orderStatus);

	boolean existsById(Integer id);
	
	Optional<Order> findByIdAndUserId(Integer id, Integer userId);
}

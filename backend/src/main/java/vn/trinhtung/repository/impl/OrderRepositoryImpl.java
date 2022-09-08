package vn.trinhtung.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

import vn.trinhtung.entity.Order;
import vn.trinhtung.entity.OrderStatus;

@Component
public class OrderRepositoryImpl {
	@PersistenceContext
	private EntityManager entityManager;

	public Page<Order> filter(OrderStatus orderStatus, String search, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> root = cq.from(Order.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (orderStatus != null) {
			Predicate p1 = cb.equal(root.get("status").as(OrderStatus.class), orderStatus);
			predicates.add(p1);
		}
		if (search != null && !search.isBlank()) {
			Predicate p2 = cb.like(root.get("id"), "%" + search + "%");
			Predicate p3 = cb.like(root.get("phoneNumber"), "%" + search + "%");
			Predicate p4 = cb.like(root.get("address"), "%" + search + "%");
			Predicate p5 = cb.like(root.get("fullName"), "%" + search + "%");

			predicates.add(cb.or(p2, p3, p4, p5));
		}
		cq.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
		
		TypedQuery<Order> query = entityManager
				.createQuery(cq.where(predicates.toArray(new Predicate[predicates.size()])));

		query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());

		CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
		cqCount.select(cb.count(cqCount.from(Order.class)));

		long totalRows = entityManager.createQuery(cqCount).getSingleResult();

		Page<Order> result = new PageImpl<Order>(query.getResultList(), pageable, totalRows);

		return result;
	}
}

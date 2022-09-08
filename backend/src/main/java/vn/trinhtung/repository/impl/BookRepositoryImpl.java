package vn.trinhtung.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import vn.trinhtung.dto.BookFilter;
import vn.trinhtung.entity.Author;
import vn.trinhtung.entity.Book;

@Component
public class BookRepositoryImpl {
	@PersistenceContext
	private EntityManager entityManager;

	public Page<Book> findAllByCategory(List<Integer> ids, BookFilter bookFilter,
			Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> root = cq.from(Book.class);
		List<Predicate> predicates = new ArrayList<>();

		Predicate p1 = root.get("category").get("id").in(ids);
		Predicate p2 = cb.equal(root.get("status"), true);

		predicates.add(p1);
		predicates.add(p2);

		if (bookFilter.getPrices() != null && bookFilter.getPrices().length > 0) {
			List<Predicate> pricePredicates = new ArrayList<>();
			for (String price : bookFilter.getPrices()) {
				Integer min = Integer.valueOf(price.split("-")[0]);
				Integer max = Integer.valueOf(price.split("-")[1]);
				System.out.println(min + " " + max);
				Predicate p3 = cb.equal(root.get("discount").as(Boolean.class), true);
				Predicate p4 = cb.between(root.get("discountPrice"), min, max);

				Predicate p5 = cb.equal(root.get("discount").as(Boolean.class), false);
				Predicate p6 = cb.between(root.get("price"), min, max);

				pricePredicates.add(cb.or(cb.and(p3, p4), cb.and(p5, p6)));
			}
			predicates.add(cb.or(pricePredicates.toArray(new Predicate[pricePredicates.size()])));
		}

		if (bookFilter.getPublishers() != null && bookFilter.getPublishers().length > 0) {
			predicates.add(root.get("publisher").get("id").in(bookFilter.getPublishers()));
		}

		if (bookFilter.getAuthors() != null && bookFilter.getAuthors().length > 0) {
			ListJoin<Book, Author> join = root.joinList("authors");
			predicates.add(join.get("id").in(bookFilter.getAuthors()));
		}

		setPredicate(predicates, root, bookFilter, cb);

		if (bookFilter.getSort() == 1) {
			cq.orderBy(cb.desc(root.get("id")));
		} else if (bookFilter.getSort() == 2) {
			cq.orderBy(cb.asc(root.get("id")));
		} else if (bookFilter.getSort() == 3) {
			Expression<Object> caseExpression = cb.selectCase()
					.when(cb.equal(root.get("discount"), true), root.get("discountPrice"))
					.otherwise(root.get("price"));
			cq.orderBy(cb.asc(caseExpression));
		} else if (bookFilter.getSort() == 4) {
			Expression<Object> caseExpression = cb.selectCase()
					.when(cb.equal(root.get("discount"), true), root.get("discountPrice"))
					.otherwise(root.get("price"));
			cq.orderBy(cb.desc(caseExpression));
		}

		cq.distinct(true);
		TypedQuery<Book> query = entityManager.createQuery(
				cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()]))));

		query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());

		List<Predicate> predicatesCount = new ArrayList<>();

		predicatesCount.add(p1);
		predicatesCount.add(p2);

		CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
		Root<Book> rootCount = cqCount.from(Book.class);
		setPredicate(predicatesCount, rootCount, bookFilter, cb);
		cqCount.select(cb.count(root));
		cqCount.where(cb.and(predicatesCount.toArray(new Predicate[predicatesCount.size()])));
		long totalRows = entityManager.createQuery(cqCount).getSingleResult();

		Page<Book> result = new PageImpl<Book>(query.getResultList(), pageable, totalRows);

		return result;
	}

	private void setPredicate(List<Predicate> predicates, Root<?> root, BookFilter bookFilter,
			CriteriaBuilder cb) {
		if (bookFilter.getPrices() != null && bookFilter.getPrices().length > 0) {
			List<Predicate> pricePredicates = new ArrayList<>();
			for (String price : bookFilter.getPrices()) {
				Integer min = Integer.valueOf(price.split("-")[0]);
				Integer max = Integer.valueOf(price.split("-")[1]);
				System.out.println(min + " " + max);
				Predicate p3 = cb.equal(root.get("discount").as(Boolean.class), true);
				Predicate p4 = cb.between(root.get("discountPrice"), min, max);

				Predicate p5 = cb.equal(root.get("discount").as(Boolean.class), false);
				Predicate p6 = cb.between(root.get("price"), min, max);

				pricePredicates.add(cb.or(cb.and(p3, p4), cb.and(p5, p6)));
			}
			predicates.add(cb.or(pricePredicates.toArray(new Predicate[pricePredicates.size()])));
		}

		if (bookFilter.getPublishers() != null && bookFilter.getPublishers().length > 0) {
			predicates.add(root.get("publisher").get("id").in(bookFilter.getPublishers()));
		}

		if (bookFilter.getAuthors() != null && bookFilter.getAuthors().length > 0) {
			ListJoin<Book, Author> join = root.joinList("authors");
			predicates.add(join.get("id").in(bookFilter.getAuthors()));
		}
	}

	public Page<Book> search(String search, BookFilter bookFilter, Pageable pageable) {
		String query = "SELECT b.id as id, b.name as name, b.slug as slug, b.thumbnail as thumbnail, "
				+ "b.in_stock as inStock, b.price as price,"
				+ "b.discount as discount, b.discount_price as discountPrice FROM book b";
		String where = "";
		String join = "";

		System.out.println(bookFilter.toString());

		if (search != null && !search.isBlank()) {
//			where += " where to_tsquery(:search) @@ to_tsvector(b.name) ";
			where += " where MATCH (b.name) against (:search) ";
		}

		if (bookFilter.getPublishers() != null && bookFilter.getPublishers().length > 0) {
			join += " join publisher p on b.publisher_id = p.id";
			where += " and p.id in (:publisherIds)";
		}

		if (bookFilter.getAuthors() != null && bookFilter.getAuthors().length > 0) {
			join += " join author_book ab on ab.book_id = b.id";
			where += " and ab.author_id in (:authorIds)";
		}

		if (bookFilter.getPrices() != null && bookFilter.getPrices().length > 0) {
			List<String> list = new ArrayList<>();
			for (String price : bookFilter.getPrices()) {
				Integer min = Integer.valueOf(price.split("-")[0]);
				Integer max = Integer.valueOf(price.split("-")[1]);
				String temp = "";
				temp += String.format(
						" ((b.discount = true and b.discount_price between %d and %d) or ", min,
						max);
				temp += String.format(" (b.discount = false and b.price between %d and %d)) ", min,
						max);
				list.add(temp);
			}
			if (list.size() > 0) {
				where += " and ( " + String.join(" or ", list) + " ) ";
			}
		}

		query += join + where;

		if (bookFilter.getSort() == 1) {
			query += " order by b.id desc";
		} else if (bookFilter.getSort() == 2) {
			query += " order by b.id asc";
		} else if (bookFilter.getSort() == 3) {
			query += " order by case when b.discount = true then discount_price else price end asc";
		} else if (bookFilter.getSort() == 4) {
			query += " order by case when b.discount = true then discount_price else price end desc";
		}

		String countQueryString = "select count(b.id) from book b";
		countQueryString += join + where;
		Query countQuery = entityManager.createNativeQuery(countQueryString);

		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.setParameter("search", search);
		countQuery.setParameter("search", search);
		if (bookFilter.getPublishers() != null && bookFilter.getPublishers().length > 0) {
			nativeQuery.setParameter("publisherIds", Arrays.asList(bookFilter.getPublishers()));
			countQuery.setParameter("publisherIds", Arrays.asList(bookFilter.getPublishers()));
		}

		if (bookFilter.getAuthors() != null && bookFilter.getAuthors().length > 0) {
			nativeQuery.setParameter("authorIds", Arrays.asList(bookFilter.getAuthors()));
			countQuery.setParameter("authorIds", Arrays.asList(bookFilter.getAuthors()));
		}
		nativeQuery.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize());

		List<Object[]> list = nativeQuery.getResultList();
		List<Book> result = new ArrayList<>();
		for (Object[] objects : list) {
			Book book = new Book();
			book.setId((Integer) objects[0]);
			book.setName((String) objects[1]);
			book.setSlug((String) objects[2]);
			book.setThumbnail((String) objects[3]);
			book.setInStock((Boolean) objects[4]);
			book.setPrice((Integer) objects[5]);
			book.setDiscount((Boolean) objects[6]);
			book.setDiscountPrice((Integer) objects[7]);

			result.add(book);
		}
		System.out.println(list.size());
		BigInteger count = (BigInteger) countQuery.getSingleResult();
		System.out.println(count);
		return new PageImpl<>(result, pageable, count.longValue());
	}
}

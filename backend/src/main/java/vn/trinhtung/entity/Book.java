package vn.trinhtung.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true, length = 255, nullable = false)
	private String name;
	@Column(unique = true, length = 50, nullable = false)
	private String slug;

	@Column(length = 255, nullable = false)
	private String thumbnail;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookImage> images = new ArrayList<>();

	@Column(columnDefinition = "boolean default false")
	private Boolean inStock;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(nullable = false)
	private Integer price;

	@Column(columnDefinition = "boolean default false")
	private Boolean discount;

	private Integer discountPrice;

	@Column(columnDefinition = "text")
	private String description;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attribute> attributes = new ArrayList<>();

	private Integer totalPages;
	private Integer width;
	private Integer height;
	private Integer length;
	private Boolean status;

	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	@Column(length = 4)
	private String publishingYear;

	@ManyToMany
	@JoinTable(
			name = "author_book", joinColumns = @JoinColumn(
					name = "book_id"
			), inverseJoinColumns = @JoinColumn(name = "author_id")
	)
	private List<Author> authors = new ArrayList<>();

}

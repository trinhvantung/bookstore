package vn.trinhtung.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "`attribute`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attribute {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "`key`")
	private String key;
	@Column(name = "`value`")
	private String value;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
}

package vn.trinhtung.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "`role`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String name;

	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();

	public Role(Integer id) {
		super();
		this.id = id;
	}

}

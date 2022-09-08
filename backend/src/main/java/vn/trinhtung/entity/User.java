package vn.trinhtung.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(length = 100, nullable = false)
	private String fullname;

	@Column(length = 64, nullable = false)
	private String password;
	
	private Boolean status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role", joinColumns = @JoinColumn(
					name = "user_id"
			), inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<Role> roles = new ArrayList<>();
}

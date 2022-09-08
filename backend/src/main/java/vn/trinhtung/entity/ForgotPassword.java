package vn.trinhtung.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ForgotPassword {
	@Id
	@Column(length = 64)
	private String code;
	
	@Column(length = 8)
	private String newPassword;
	private Date expire;

	@ManyToOne
	private User user;
}

package vn.trinhtung.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "`order`")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderTrack> orderTracks = new ArrayList<OrderTrack>();

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(length = 50, nullable = false)
	private String fullName;

	@Column(length = 255, nullable = false)
	private String address;

	@Column(length = 15, nullable = false)
	private String phoneNumber;
	
	private Integer totalPrice;

	private String note;
	@CreatedDate
	private Date createdDate;
}

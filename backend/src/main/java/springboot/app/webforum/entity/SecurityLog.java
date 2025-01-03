package springboot.app.webforum.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * The persistent class for the security_logs database table.
 * 
 */
@Entity
@Table(name="security_logs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String context;

	@Column(name="logged_at")
	private Timestamp loggedAt;

	@Column(name="http_method")
	private String httpMethod;
	
	private String uri;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
}
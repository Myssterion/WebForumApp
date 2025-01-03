package springboot.app.webforum.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	private String name;

	private String password;

	@Column(name = "registered_at")
	private Timestamp registeredAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;

	private String surname;

	private String username;
	
	@Column(name = "verification_code")
	private String verificationCode;
	
	@Column(name = "code_expires_at")
	private Timestamp codeExpiresAt;

	// bi-directional many-to-one association to Role
	@ManyToOne(fetch = FetchType.EAGER)
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Adjust based on your business logic
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Adjust based on your business logic
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Adjust based on your business logic
	}

	@Override
	public boolean isEnabled() {
		return true; // Adjust based on your business logic
	}

}
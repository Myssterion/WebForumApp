package springboot.app.webforum.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * The persistent class for the permissions database table.
 * 
 */
@Entity
@Table(name="permissions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="permission_name")
	private String permissionName;
}
package springboot.app.webforum.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the user_topic_permissions database table.
 * 
 */
@Entity
@Table(name = "user_topic_permissions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTopicPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserTopicPermissionPK id;

	// bi-directional many-to-one association to Permission
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("permissionId")
	private Permission permission;

	// bi-directional many-to-one association to Topic
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("topicId")
	private Topic topic;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;
}
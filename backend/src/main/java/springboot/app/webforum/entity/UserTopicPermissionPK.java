package springboot.app.webforum.entity;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the user_topic_permissions database table.
 * 
 */
@Embeddable
public class UserTopicPermissionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id")
	private int userId;

	@Column(name="permission_id")
	private int permissionId;

	@Column(name="topic_id")
	private int topicId;

	public UserTopicPermissionPK() {
	}
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPermissionId() {
		return this.permissionId;
	}
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}
	public int getTopicId() {
		return this.topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserTopicPermissionPK)) {
			return false;
		}
		UserTopicPermissionPK castOther = (UserTopicPermissionPK)other;
		return 
			(this.userId == castOther.userId)
			&& (this.permissionId == castOther.permissionId)
			&& (this.topicId == castOther.topicId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.permissionId;
		hash = hash * prime + this.topicId;
		
		return hash;
	}
}
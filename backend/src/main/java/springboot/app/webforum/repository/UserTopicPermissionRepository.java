package springboot.app.webforum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.UserTopicPermission;
import springboot.app.webforum.entity.UserTopicPermissionPK;

@Repository
public interface UserTopicPermissionRepository extends JpaRepository<UserTopicPermission, UserTopicPermissionPK> {

	List<UserTopicPermission> findByUserId(Integer userId);

	List<UserTopicPermission> findByIdUserIdAndIdTopicId(Integer userId, Integer topicId);

}

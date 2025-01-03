package springboot.app.webforum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	List<Comment> findByStatusIdOrderByPostedAtDesc(Integer statusId);

    List<Comment> findTop20ByTopicIdAndStatusIdOrderByPostedAtDesc(Integer topicId, Integer commentStatusId);

	List<Comment> findByStatusIdAndTopicIdOrderByPostedAtDesc(Integer statusId, Integer topicId);

}

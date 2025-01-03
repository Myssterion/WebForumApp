package springboot.app.webforum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

	Optional<Topic> findByTopicName(String topicName);

}

package springboot.app.webforum.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.app.webforum.entity.Topic;
import springboot.app.webforum.repository.TopicRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {

	private final TopicRepository topicRepository;

	public List<Topic> findAllTopics() {
		return topicRepository.findAll();
	}

}

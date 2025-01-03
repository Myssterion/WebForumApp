package springboot.app.webforum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.entity.Topic;
import springboot.app.webforum.service.TopicService;

@Component
@RequiredArgsConstructor
public class TopicController {

	private final TopicService topicService;
	
	public ResponseEntity<List<Topic>> getTopics() {
		List<Topic> topics = topicService.findAllTopics();
		return new ResponseEntity<>(topics, HttpStatus.OK);
	}
}

package springboot.app.webforum.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.CommentDto;
import springboot.app.webforum.dto.CommentPostDto;
import springboot.app.webforum.dto.CommentStatusDto;
import springboot.app.webforum.entity.Comment;
import springboot.app.webforum.entity.Status;
import springboot.app.webforum.entity.Topic;
import springboot.app.webforum.entity.User;
import springboot.app.webforum.exception.IdMismatchException;
import springboot.app.webforum.exception.InvalidEntityAttributeException;
import springboot.app.webforum.exception.InvalidEntityIdException;
import springboot.app.webforum.repository.CommentRepository;
import springboot.app.webforum.repository.StatusRepository;
import springboot.app.webforum.repository.TopicRepository;
import springboot.app.webforum.repository.UserRepository;
import springboot.app.webforum.util.EntityToDto;

import static springboot.app.webforum.util.EntityToDto.ConvertToDto;
import static springboot.app.webforum.util.Constants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final StatusRepository statusRepository;
	private final TopicRepository topicRepository;
	private final UserRepository userRepository;

	public List<CommentDto> findNewComments(String topicName) {
		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new InvalidEntityAttributeException("No Comment with given name!"));
		
		Status status = statusRepository.findByStatusName(PENDING)
				.orElseThrow(() -> new InvalidEntityAttributeException("No CommentStatus with given name!"));

		return commentRepository.findByStatusIdAndTopicIdOrderByPostedAtDesc(status.getId(), topic.getId()).stream()
				.map(EntityToDto::ConvertToDto).collect(Collectors.toList());
	}

	public List<CommentDto> findCommentsForTopic(String topicName) {
		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new InvalidEntityAttributeException("No Comment with given name!"));
		
		Status commentStatus = statusRepository.findByStatusName(APPROVED)
				.orElseThrow(() -> new InvalidEntityAttributeException("No CommentStatus with given name!"));

		return commentRepository.findTop20ByTopicIdAndStatusIdOrderByPostedAtDesc(topic.getId(), commentStatus.getId()).stream()
				.map(EntityToDto::ConvertToDto).collect(Collectors.toList());
	}

	public void approveComment(Integer commentId) {
		changeStatus(commentId,APPROVED);
	}

	public void banComment(Integer commentId) {
		changeStatus(commentId,BANNED);

	}
	
	private void changeStatus(Integer commentId, String statusName) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new InvalidEntityIdException("No Commet with given id!"));

		Status commentStatus = statusRepository.findByStatusName(statusName)
				.orElseThrow(() -> new InvalidEntityAttributeException("No CommentStatus with given name!"));

		comment.setStatus(commentStatus);
		commentRepository.save(comment);
	}

	public CommentDto modifyAndApproveComment(Integer commentId, CommentStatusDto updatedComment) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new InvalidEntityIdException("No Commet with given id!"));
		
		Status commentStatus = statusRepository.findByStatusName(APPROVED)
				.orElseThrow(() -> new InvalidEntityAttributeException("No CommentStatus with given name!"));
		
		if(commentId != updatedComment.getCommentId())
			throw new IdMismatchException("ID in path variable does not match ID in request body.");
		
		comment.setContent(updatedComment.getContent());
		comment.setStatus(commentStatus);
		commentRepository.save(comment);
		
		return ConvertToDto(comment);
	}

	public CommentDto postComment(String topicName, CommentPostDto commentDto) {
		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new InvalidEntityAttributeException("No Comment with given name!"));
		
		Status status = statusRepository.findByStatusName(PENDING)
				.orElseThrow(() -> new InvalidEntityAttributeException("No CommentStatus with given name!"));
		
		User user = userRepository.findByUsername(commentDto.getUsername())
				.orElseThrow(() -> new InvalidEntityAttributeException("No User with given username!"));
		
		Comment comment = new Comment();
		comment.setContent(commentDto.getContent());
		comment.setStatus(status);
		comment.setTopic(topic);
		comment.setUser(user);
		comment.setPostedAt(Timestamp.valueOf(LocalDateTime.now()));
		System.out.println("HAHAHHA " + commentDto.getUsername());
		commentRepository.save(comment);
		return ConvertToDto(comment);
	}

}

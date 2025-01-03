package springboot.app.webforum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.CommentDto;
import springboot.app.webforum.dto.CommentPostDto;
import springboot.app.webforum.dto.CommentStatusDto;
import springboot.app.webforum.service.CommentService;

@Component
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	
	public ResponseEntity<List<CommentDto>> getNewComments(String topicName) {
		List<CommentDto> newComments = commentService.findNewComments(topicName);
		return new ResponseEntity<>(newComments, HttpStatus.OK);
	}

	public ResponseEntity<List<CommentDto>> getNewCommentsForTopic(String topicName) {
		List<CommentDto> topicComments = commentService.findCommentsForTopic(topicName);
		return new ResponseEntity<>(topicComments, HttpStatus.OK);
	}

	public ResponseEntity<?> postComment(String topicName, CommentPostDto comment) {
		CommentDto newComment = commentService.postComment(topicName, comment);
		if (newComment == null)
			return ResponseEntity.badRequest().build();
		else
			return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> approveComment(Integer commentId) {

		commentService.approveComment(commentId);
		return ResponseEntity.ok().build();

	}

	public ResponseEntity<?> banComment(Integer commentId) {

		commentService.banComment(commentId);
		return ResponseEntity.ok().build();

	}

	public ResponseEntity<CommentDto> modifyAndApproveComment(Integer commentId, CommentStatusDto updatedComment) {
		CommentDto comment = commentService.modifyAndApproveComment(commentId, updatedComment);
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}
}

package springboot.app.webforum.controller;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.CommentDto;
import springboot.app.webforum.dto.CommentPostDto;
import springboot.app.webforum.dto.CommentStatusDto;
import springboot.app.webforum.dto.PermissionDto;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.dto.Test;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.entity.Topic;
import springboot.app.webforum.util.MaliciousRequestEvent;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class WAF  {
	
	private final CommentController commentController;
	private final TopicController topicController;
	private final UserController userController;
	private final RoleController roleController;
	private final Validator validator;

	private final ApplicationEventPublisher eventPublisher;
	private static List<String> allowedTopics= List.of("Sport", "Music",
			"Science", "Culture");

	
	/*
	 * Comments
	 */

	public ResponseEntity<List<CommentDto>> getNewComments(String topicName) {
		if(isValidTopicName(topicName))
			return commentController.getNewComments(topicName);
		return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<List<CommentDto>> getNewCommentsForTopic(String topicName) {
		if(isValidTopicName(topicName))
			return commentController.getNewCommentsForTopic(topicName);
		return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<?> postComment(String topicName, CommentPostDto comment) {
		if(isValidTopicName(topicName) && isValidCommentPostDto(comment))
			return commentController.postComment(topicName, comment);
		return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<?> approveComment(Integer commentId) {
		if(isValidId(commentId)) 
			return commentController.approveComment(commentId);
		return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<?> banComment(Integer commentId) {
		if(isValidId(commentId)) 
			return commentController.banComment(commentId);
		return ResponseEntity.badRequest().build();
	}
	
	public ResponseEntity<?> test(Test test) {
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<CommentDto> modifyAndApproveComment(Integer commentId, CommentStatusDto updatedComment) {
		if(isValidId(commentId) && isValidCommentStatusDto(updatedComment)) 
			return commentController.modifyAndApproveComment(commentId, updatedComment);
		return ResponseEntity.badRequest().build();
	}

	/*
	 * Topic
	 */

	public ResponseEntity<List<Topic>> getTopics() {
		return topicController.getTopics();
	}
	
	/*
	 * Role
	 */
	
	public ResponseEntity<List<RoleDto>> getRoles() {
		return roleController.getRoles();
	}

	/*
	 * User
	 */

	public ResponseEntity<List<UserDto>> getAllUsers() {
		return userController.getAllUsers();
	}
	
	public ResponseEntity<List<UserDto>> getRegisteredUsers() {
		return userController.getRegisteredUsers();
	}
	
	public ResponseEntity<PermissionDto> getUserPermissionsForTopic(String topicName, String username) {
		if(isValidTopicName(topicName))
			return userController.getUserPermissionsForTopic(topicName, username);
		return ResponseEntity.badRequest().build();
	}

	
	public ResponseEntity<List<PermissionDto>> getUserPermissions(Integer userId) {
		if(isValidId(userId))
			return userController.getUserPermissions(userId);
		return ResponseEntity.badRequest().build();
	}
	
	public ResponseEntity<RoleDto> updateUserRole(Integer userId, RoleDto role) {
		if(isValidId(userId)  && isValidRoleDto(role))
			return userController.updateUserRole(userId, role);
		return ResponseEntity.badRequest().build();
	}
	
	public ResponseEntity<?> updateUserPermissions(Integer userId, List<PermissionDto> permissions) {
		if(isValidId(userId) && isValidPermissionsDto(permissions))
			return userController.updateUserPermissions(userId, permissions);
		return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<?> approveUser(Integer userId) {
		if(isValidId(userId)) 
			return userController.approveUser(userId);
		return ResponseEntity.badRequest().build();
	}

	public ResponseEntity<?> banUser(Integer userId) {
		if(isValidId(userId)) 
			return userController.banUser(userId);
		return ResponseEntity.badRequest().build();
	}

	/*
	 * Util methods
	 */
	private boolean isValidTopicName(String topicName) {
		if(allowedTopics.contains(topicName))
			return true;
		
		eventPublisher.publishEvent(new MaliciousRequestEvent(this, "Invalid topic name: " + topicName));
		return false;
	}

	private boolean isValidId(Integer id) {
		if(id != null && id > 0)
			return true;
		
		eventPublisher.publishEvent(new MaliciousRequestEvent(this, "Invalid ID: " + id));
		return false;
	}
	
/*	private boolean isValidCommentDto(CommentDto comment) {
		 Errors errors = new BeanPropertyBindingResult(comment, "commentDto");
		 validator.validate(comment, errors);
	     if(!errors.hasErrors())
	    	 return true;
	     
	     String errorMessage = "";
	     for(var error : errors.getAllErrors())
	    	 errorMessage += errorMessage.equals("") ? error.getDefaultMessage() : (" && " + error.getDefaultMessage());
 	     eventPublisher.publishEvent(new MaliciousRequestEvent(this, errorMessage));
 	     return false;
	}
	*/
	private boolean isValidCommentPostDto(CommentPostDto comment) {
		 Errors errors = new BeanPropertyBindingResult(comment, "commentPostDto");
		 validator.validate(comment, errors);
	     if(!errors.hasErrors())
	    	 return true;
	     
	     String errorMessage = "";
	     for(var error : errors.getAllErrors())
	    	 errorMessage += errorMessage.equals("") ? error.getDefaultMessage() : (" && " + error.getDefaultMessage());
 	     eventPublisher.publishEvent(new MaliciousRequestEvent(this, errorMessage));
 	     return false;
	}
	
	private boolean isValidCommentStatusDto(CommentStatusDto updatedComment) {
		 Errors errors = new BeanPropertyBindingResult(updatedComment, "commentStatusDto");
		 validator.validate(updatedComment, errors);
		 if(!errors.hasErrors())
	    	 return true;
	     
	     String errorMessage = "";
	     for(var error : errors.getAllErrors())
	    	 errorMessage += errorMessage.equals("") ? error.getDefaultMessage() : (" && " + error.getDefaultMessage());
 	     eventPublisher.publishEvent(new MaliciousRequestEvent(this, errorMessage));
 	     return false;
	}
	
	private boolean isValidRoleDto(RoleDto role) {
		 Errors errors = new BeanPropertyBindingResult(role, "roleDto");
		 validator.validate(role, errors);
	     if(!errors.hasErrors())
	    	 return true;
	     
	     String errorMessage = "";
	     for(var error : errors.getAllErrors())
	    	 errorMessage += errorMessage.equals("") ? error.getDefaultMessage() : (" && " + error.getDefaultMessage());
 	     eventPublisher.publishEvent(new MaliciousRequestEvent(this, errorMessage));
 	     return false;
	}
	
	private boolean isValidPermissionsDto(List<PermissionDto> permissionsDto) { 
		 Errors errors = new BeanPropertyBindingResult(permissionsDto, "permissionsDto");
		 
		 validator.validate(permissionsDto, errors);
	     if(!errors.hasErrors())
	    	 return true;
	     
	     String errorMessage = "";
	     for(var error : errors.getAllErrors())
	    	 errorMessage += errorMessage.equals("") ? error.getDefaultMessage() : (" && " + error.getDefaultMessage());
 	     eventPublisher.publishEvent(new MaliciousRequestEvent(this, errorMessage));
 	     return false;
	}
}

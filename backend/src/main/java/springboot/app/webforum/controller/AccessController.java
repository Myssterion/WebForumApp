package springboot.app.webforum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.AuthRequest;
import springboot.app.webforum.dto.CommentDto;
import springboot.app.webforum.dto.CommentPostDto;
import springboot.app.webforum.dto.CommentStatusDto;
import springboot.app.webforum.dto.PermissionDto;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.dto.Test;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.dto.VerifyRequest;
import springboot.app.webforum.entity.Topic;
import springboot.app.webforum.service.TokenBlacklistService;


@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AccessController {

	private final AuthenticationController authController;
	private final TokenBlacklistService tokenBlacklistService;
	private SecurityContextHolder securityContextHolder;
	private final WAF waf;

	public void handleMaliciousRequest() {
		 Authentication authentication = securityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.getCredentials() != null) {
	            String token = authentication.getCredentials().toString();
	            System.out.println("MALICIOUS");
	            tokenBlacklistService.blacklistToken(token);
	        }
	}
	/*
	 * Authentication
	 */

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		return authController.login(authRequest);
	}

	@PostMapping("/auth/verify")
	public ResponseEntity<?> verify(@RequestBody VerifyRequest verifyRequest) {
		return authController.verify(verifyRequest);
	}
	
	@GetMapping("/auth/retrieve/jwt/{temp}") 
	public ResponseEntity<?> retreive(@PathVariable String temp) {
		return authController.retreive(temp);
	}

	@PostMapping("/auth/register")
	public ResponseEntity<?> login(@RequestBody UserDto registerRequest) {
		return authController.register(registerRequest);
	}

	
	/*
	 * Comments
	 */

	@GetMapping("/comment/find/new/{topicName}")
	public ResponseEntity<List<CommentDto>> getNewCommentsToManage(@PathVariable String topicName) {
		return waf.getNewComments(topicName);
	}

	@GetMapping("/comment/find/topic/{topicName}")
	public ResponseEntity<List<CommentDto>> getNewComments(@PathVariable String topicName) {
		ResponseEntity<List<CommentDto>> response = waf.getNewCommentsForTopic(topicName);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}

	@PostMapping("/comment/post/new/{topicName}") 
	public ResponseEntity<?> postComment(@PathVariable String topicName, @RequestBody CommentPostDto comment) {
		ResponseEntity<?> response = waf.postComment(topicName, comment);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}

	@PostMapping("/comment/update/approve/{commentId}")
	public ResponseEntity<?> approveComment(@PathVariable Integer commentId) {
		ResponseEntity<?> response = waf.approveComment(commentId);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}

	@PostMapping("/comment/update/ban/{commentId}")
	public ResponseEntity<?> banComment(@PathVariable Integer commentId) {
		ResponseEntity<?> response = waf.banComment(commentId);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}
	
	@PutMapping("/test/find")
	public ResponseEntity<?> test(Test test) {
		return ResponseEntity.ok().build();
	}

	@PutMapping("/comment/update/modify/{commentId}")
	public ResponseEntity<CommentDto> modifyAndApproveComment(@PathVariable Integer commentId, @RequestBody CommentStatusDto updatedComment) {
		ResponseEntity<CommentDto> response = waf.modifyAndApproveComment(commentId, updatedComment);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}

	/*
	 * Topic
	 */

	@GetMapping("/topic/find/all")
	public ResponseEntity<List<Topic>> getTopics() {
		return waf.getTopics();
	}
	
	/*
	 * Role
	 */
	
	@GetMapping("/role/find/all")
	public ResponseEntity<List<RoleDto>> getRoles() {
		return waf.getRoles();
	}

	/*
	 * User
	 */
	
	@GetMapping("/user/find/all")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return waf.getAllUsers();
	}

	@GetMapping("/user/find/registered")
	public ResponseEntity<List<UserDto>> getRegisteredUsers() {
		return waf.getRegisteredUsers();
	}
	
	@GetMapping("/user/find/permission/{userId}")
	public ResponseEntity<List<PermissionDto>> getUserPermissions(@PathVariable Integer userId) {
		return waf.getUserPermissions(userId);
	}
	
	@GetMapping("/user/permission/for/{topicName}")
	public ResponseEntity<PermissionDto> getUserPermissionsForTopic(@PathVariable String topicName, @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		return waf.getUserPermissionsForTopic(topicName, username);
	}
	
	@PutMapping("/user/update/role/{userId}")
	public ResponseEntity<RoleDto> updateUserRole(@PathVariable Integer userId,@RequestBody RoleDto role) {
		return waf.updateUserRole(userId, role);
	}
	
	@PutMapping("/user/update/permission/{userId}")
	public ResponseEntity<?> updateUserPermissions(@PathVariable Integer userId, @RequestBody List<PermissionDto> permissions) {
		return waf.updateUserPermissions(userId, permissions);
	}

	@PostMapping("/user/update/approve/{userId}")
	public ResponseEntity<?> approveUser(@PathVariable Integer userId) {
		ResponseEntity<?> response = waf.approveUser(userId);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}

	@PostMapping("/user/update/ban/{userId}")
	public ResponseEntity<?> banUser(@PathVariable Integer userId) {
		ResponseEntity<?> response = waf.banUser(userId);
		if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
			handleMaliciousRequest();
		return response;
	}
}

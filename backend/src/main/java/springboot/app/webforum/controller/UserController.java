package springboot.app.webforum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.PermissionDto;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userService.findAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	

	public ResponseEntity<List<UserDto>> getRegisteredUsers() {
		List<UserDto> registeredUsers = userService.findRegisteredUsers();
		return new ResponseEntity<>(registeredUsers, HttpStatus.OK);
	}

	public ResponseEntity<?> approveUser(Integer userId) {
		userService.approveUser(userId);
		return ResponseEntity.ok().build();

	}

	public ResponseEntity<?> banUser(Integer userId) {
		userService.banUser(userId);
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<List<PermissionDto>> getUserPermissions(Integer userId) {
		List<PermissionDto> permissions = userService.findUserPermissions(userId);
		return new ResponseEntity<>(permissions, HttpStatus.OK);
	}
	
	public ResponseEntity<RoleDto> updateUserRole(Integer userId, RoleDto role) {
		RoleDto roleDto = userService.updateUserRole(userId, role);
		return new ResponseEntity<>(roleDto, HttpStatus.OK);
	}
	
	public ResponseEntity<?> updateUserPermissions(Integer userId, List<PermissionDto> permissions) {
		userService.updateUserPermissions(userId, permissions);
		return ResponseEntity.ok().build();
	}


	public ResponseEntity<PermissionDto> getUserPermissionsForTopic(String topicName, String username) {
		PermissionDto permissionDto = userService.findUserPermissionForTopic(topicName, username);
		return new ResponseEntity<>(permissionDto, HttpStatus.OK);
	}
}

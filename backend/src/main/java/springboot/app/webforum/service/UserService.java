package springboot.app.webforum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.PermissionDto;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.entity.Role;
import springboot.app.webforum.entity.Status;
import springboot.app.webforum.entity.Permission;
import springboot.app.webforum.entity.Topic;
import springboot.app.webforum.entity.User;
import springboot.app.webforum.entity.UserTopicPermission;
import springboot.app.webforum.entity.UserTopicPermissionPK;
import springboot.app.webforum.exception.InvalidEntityAttributeException;
import springboot.app.webforum.exception.InvalidEntityIdException;
import springboot.app.webforum.exception.InvalidUserRoleException;
import springboot.app.webforum.repository.RoleRepository;
import springboot.app.webforum.repository.StatusRepository;
import springboot.app.webforum.repository.TopicRepository;
import springboot.app.webforum.repository.UserRepository;
import springboot.app.webforum.repository.UserTopicPermissionRepository;
import springboot.app.webforum.repository.PermissionRepository;
import springboot.app.webforum.util.EntityToDto;

import static springboot.app.webforum.util.Constants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final TopicRepository topicRepository;
	private final StatusRepository statusRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final UserTopicPermissionRepository userTopicPermissionRepository;
	private final EmailService emailService;
	private final List<PermissionDto> allPermissionsModerator = new ArrayList<>();
	private final List<PermissionDto> allPermissionsUser = new ArrayList<>();
	private boolean permissionsLoaded = false;

	private void loadAllPermissions() {
		loadPermissions(allPermissionsModerator, MODERATOR);
		loadPermissions(allPermissionsUser, USER);
	}

	private void loadPermissions(List<PermissionDto> permissions, String userRole) {
		List<Topic> topics = topicRepository.findAll();

		Role role = roleRepository.findByRoleName(userRole)
				.orElseThrow(() -> new InvalidEntityAttributeException("No role with given name!"));
		List<String> permissionNames = role.getPermissions().stream()
														.map(o -> o.getPermissionName())
														.collect(Collectors.toList());
		
		for (var topic : topics) {
			permissions.add(PermissionDto.builder()
					.topicName(topic.getTopicName())
					.permissions(permissionNames)
					.build());
		}

	}
	
	public List<UserDto> findAllUsers() {
		Role role = roleRepository.findByRoleName(ADMIN)
				.orElseThrow(() -> new InvalidEntityAttributeException("No role with given name!"));
		
		return userRepository.findByRoleIdNot(role.getId()).stream().map(EntityToDto::ConvertToDto)
								.collect(Collectors.toList());
	}

	public List<UserDto> findRegisteredUsers() {
		Status status = statusRepository.findByStatusName(PENDING)
				.orElseThrow(() -> new InvalidEntityAttributeException("No status with given name!"));

		return userRepository.findByStatusIdOrderByRegisteredAt(status.getId()).stream().map(EntityToDto::ConvertToDto)
				.collect(Collectors.toList());
	}
	
	public PermissionDto findUserPermissionForTopic(String topicName, String username) {
		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new InvalidEntityAttributeException("No permission with given name!"));
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new InvalidEntityIdException("No user with given username!"));
		
		
		List<String> permissionNames = user.getRole().getPermissions().stream()
				.map(o -> o.getPermissionName())
				.collect(Collectors.toList());
		
		
		List<UserTopicPermission> userTopicPermissions = userTopicPermissionRepository.findByIdUserIdAndIdTopicId(user.getId(),topic.getId());
		
		for(var revoked : userTopicPermissions) 
			if(permissionNames.contains(revoked.getPermission().getPermissionName()))
				permissionNames.remove(revoked.getPermission().getPermissionName());
		
		PermissionDto permissionDto = PermissionDto.builder()
				.topicName(topic.getTopicName())
				.permissions(permissionNames)
				.build();
		
		return permissionDto;
	}

	public void approveUser(Integer userId) {
		changeStatus(userId, APPROVED);
	}

	public void banUser(Integer userId) {
		changeStatus(userId, BANNED);
	}

	public void changeStatus(Integer userId, String statusName) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidEntityIdException("No user with given id!"));

		Status status = statusRepository.findByStatusName(statusName)
				.orElseThrow(() -> new InvalidEntityAttributeException("No status with given name!"));
		
		emailService.sendAccountStatusMessage(user.getEmail(), statusName);

		user.setStatus(status);
		userRepository.save(user);

	}

	public List<PermissionDto> findUserPermissions(Integer userId) { 
		if(!permissionsLoaded) {
			permissionsLoaded = true;
			loadAllPermissions();
		}
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidEntityIdException("No user with given id!"));
		
		List<UserTopicPermission> userTopicPermissions = userTopicPermissionRepository.findByUserId(userId);
		
		if(userTopicPermissions.isEmpty()) {
			if(user.getRole().getRoleName().equals(MODERATOR))
				return allPermissionsModerator;
			if(user.getRole().getRoleName().equals(USER))
				return allPermissionsUser;
			else 
				throw new InvalidUserRoleException("User isn't MODERATOR or USER!");
		}
		
		List<PermissionDto> defaultPermissions;
		if(user.getRole().getRoleName().equals(MODERATOR)) {
			if(userTopicPermissions.isEmpty())
				return allPermissionsModerator;
			
			defaultPermissions = allPermissionsModerator.stream()
			.map(PermissionDto::new)
			.collect(Collectors.toList());
		} 
		else if(user.getRole().getRoleName().equals(USER)) {
			if(userTopicPermissions.isEmpty())
				return allPermissionsUser;

			defaultPermissions = allPermissionsUser.stream()
			.map(PermissionDto::new)
			.collect(Collectors.toList());
		}
		else 
			throw new InvalidUserRoleException("User isn't MODERATOR or USER!");
		
		for(var entry : userTopicPermissions) {
			for(var permission : defaultPermissions)
				if(permission.getTopicName().equals(entry.getTopic().getTopicName())) {
					permission.getPermissions().remove(entry.getPermission().getPermissionName());
				}
		}
		return defaultPermissions;
	}

	public RoleDto updateUserRole(Integer userId, RoleDto roleDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidEntityIdException("No user with given id!"));

		Role role = roleRepository.findById(roleDto.getRoleId())
				.orElseThrow(() -> new InvalidEntityIdException("No role with given id!"));
		
		user.setRole(role);
		return EntityToDto.ConvertToDto(userRepository.save(user).getRole());
	}

	public void updateUserPermissions(Integer userId, List<PermissionDto> permissions) {
		if(!permissionsLoaded) {
			permissionsLoaded = true;
			loadAllPermissions();
		}
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidEntityIdException("No user with given id!"));
		
		List<PermissionDto> defaultPermissions;
		if(user.getRole().getRoleName().equals(MODERATOR))
			defaultPermissions = allPermissionsModerator;
		else if(user.getRole().getRoleName().equals(USER))
			defaultPermissions = allPermissionsUser;
		else 
			throw new InvalidUserRoleException("User has invalid role!");
			
		List<PermissionDto> revokedPermissions = findRevokedPermissions(defaultPermissions, permissions);
		List<UserTopicPermission> userTopicPermissions = userTopicPermissionRepository.findByUserId(userId);
		
		for(var entry : userTopicPermissions) {
			for(var permission : revokedPermissions)
				if(permission.getTopicName().equals(entry.getTopic().getTopicName()) && !permission.getPermissions().contains(entry.getPermission().getPermissionName())) {
					userTopicPermissionRepository.delete(entry);
				}
		}
		
		
		for(var entry : revokedPermissions) {
			Topic topic = topicRepository.findByTopicName(entry.getTopicName())
					.orElseThrow(() -> new InvalidEntityAttributeException("No permission with given name!"));
			
			for(var revokedPermission : entry.getPermissions()) {
				Permission permission = permissionRepository.findByPermissionName(revokedPermission)
						.orElseThrow(() -> new InvalidEntityAttributeException("No permission with given name!"));
				
				UserTopicPermissionPK pk = new UserTopicPermissionPK();
				pk.setPermissionId(permission.getId());
				pk.setTopicId(topic.getId());
				pk.setUserId(user.getId()); 
				
				UserTopicPermission utp = userTopicPermissionRepository.findById(pk).orElse(null);
				if(utp == null) {
				UserTopicPermission userTopicPermission = UserTopicPermission.builder()
																			.id(pk)
																			.user(user)
																			.topic(topic)
																			.permission(permission)
																			.build();
				
				userTopicPermissionRepository.save(userTopicPermission);
				}
			}
		}
	}

	private List<PermissionDto> findRevokedPermissions(List<PermissionDto> defaultPermissions, List<PermissionDto> userPermissions) {
		List<PermissionDto> revokedPermissions = new ArrayList<>();
		Map<String, List<String>> userPermissionsMap = userPermissions.stream()
				.collect(Collectors.toMap(PermissionDto::getTopicName, PermissionDto::getPermissions));
		
		for(var permission : defaultPermissions) {
			List<String> currPermissions = userPermissionsMap.get(permission.getTopicName());
			List<String> revokedPerms = permission.getPermissions().stream()
					.filter(perm -> !currPermissions.contains(perm))
					.collect(Collectors.toList());
			
			revokedPermissions.add(new PermissionDto(permission.getTopicName(), revokedPerms));
		}
		
		return revokedPermissions;
	}
}

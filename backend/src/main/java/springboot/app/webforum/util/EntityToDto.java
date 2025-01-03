package springboot.app.webforum.util;

import springboot.app.webforum.dto.CommentDto;
import springboot.app.webforum.dto.CommentStatusDto;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.entity.Comment;
import springboot.app.webforum.entity.Role;
import springboot.app.webforum.entity.User;

public class EntityToDto {

	public static CommentDto ConvertToDto(Comment comment) {
		CommentDto commentDto = new CommentDto();
		commentDto.setCommentId(comment.getId());
		commentDto.setContent(comment.getContent());
		commentDto.setPosted(comment.getPostedAt());
		commentDto.setUsername(comment.getUser().getUsername());
		return commentDto;
	}
	
	public static CommentStatusDto ConvertToDtoStatus(Comment comment) {
		CommentStatusDto commentStatusDto = new CommentStatusDto();
		commentStatusDto.setCommentId(comment.getId());
		commentStatusDto.setContent(comment.getContent());
		return commentStatusDto;
	}
	
	public static UserDto ConvertToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		userDto.setSurname(user.getSurname());
		userDto.setUsername(user.getUsername());
		userDto.setUserId(user.getId());
		userDto.setRole(ConvertToDto(user.getRole()));
		return userDto;
	}
	
	public static RoleDto ConvertToDto(Role role) {
		RoleDto roleDto = new RoleDto();
		roleDto.setRoleId(role.getId());
		roleDto.setRoleName(role.getRoleName());
		return roleDto;
	}
	

}

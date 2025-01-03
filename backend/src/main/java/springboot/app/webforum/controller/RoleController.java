package springboot.app.webforum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.service.RoleService;

@Component
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;
	
	public ResponseEntity<List<RoleDto>> getRoles() {
		List<RoleDto> roles = roleService.getRoles();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
}

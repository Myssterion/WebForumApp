package springboot.app.webforum.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.dto.RoleDto;
import springboot.app.webforum.entity.Role;
import springboot.app.webforum.repository.RoleRepository;
import springboot.app.webforum.util.EntityToDto;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	public List<RoleDto> getRoles() {
		List<Role> roles = roleRepository.findAll();
		return roles.stream()
					.map(EntityToDto::ConvertToDto)
					.collect(Collectors.toList());
	}

}

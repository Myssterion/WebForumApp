package springboot.app.webforum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private int userId;
	private String name;
	private String surname;
	private String username;
	private String password;
	private String email;
	private RoleDto role;

}

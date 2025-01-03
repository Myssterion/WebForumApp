package springboot.app.webforum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.app.webforum.util.NoSQLInjection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

	@NotNull(message = "Role ID cannot be null")
	@Positive(message = "Role ID cannot be negative or zero")
	private int roleId;
	@NotBlank(message = "Role name cannot be null or blank")
	@Size(min = 4, max = 45, message = "Role name size must be between 4 and 45")
	@NoSQLInjection(message = "Role name cannot contain SQL Injection patterns")
	private String roleName;
}

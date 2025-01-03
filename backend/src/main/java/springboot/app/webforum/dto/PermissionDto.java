package springboot.app.webforum.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PermissionDto {

	@NotBlank(message = "Topic name cannot be null or blank")
	@Size(min = 4, max = 45, message = "Topic name size must be between 4 and 45")
	@NoSQLInjection(message = "Topic name cannot contain SQL Injection patterns")
	private String topicName;
	@NotNull(message = "Permissions list cannot be null")
	private List<@NotBlank(message = "Permission name cannot be null or blank")
	@NoSQLInjection(message = "Permission name cannot contain SQL Injection patterns") String> permissions;

	public PermissionDto(PermissionDto other) {
        this.topicName = other.topicName;
        this.permissions = new ArrayList<>(other.permissions);
    }
}

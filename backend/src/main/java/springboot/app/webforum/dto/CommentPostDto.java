package springboot.app.webforum.dto;

import jakarta.validation.constraints.NotBlank;
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
public class CommentPostDto {


	@NotBlank(message = "Content cannot be null or blank")
	@Size(min = 2, max = 255, message = "Content size must be between 2 and 255")
	@NoSQLInjection(message = "Content cannot contain SQL Injection patterns")
	private String content;
	@NotBlank(message = "Username cannot be null or blank")
	@Size(min = 6, max = 15, message = "Username size must be between 6 and 15")
	@NoSQLInjection(message = "Username cannot contain SQL Injection patterns")
	private String username;

}

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
public class CommentStatusDto {
	

	@NotNull(message = "Comment ID cannot be null")
	@Positive(message = "Comment ID cannot be negative or zero")
	private int commentId;
	@NotBlank(message = "Content cannot be null or blank")
	@Size(min = 2, max = 255, message = "Content size must be between 2 and 255")
	@NoSQLInjection(message = "Content cannot contain SQL Injection patterns")
	private String content;

}

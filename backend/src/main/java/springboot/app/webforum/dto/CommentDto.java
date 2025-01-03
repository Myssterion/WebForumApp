package springboot.app.webforum.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.app.webforum.util.NoSQLInjection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	
	@NotNull(message = "Comment ID cannot be null")
	@Positive(message = "Comment ID cannot be negative or zero")
	private int commentId;
	//topicid mora biti za post?
	@NotBlank(message = "Content cannot be null or blank")
	@Size(min = 2, max = 255, message = "Content size must be between 2 and 255")
	@NoSQLInjection(message = "Content cannot contain SQL Injection patterns")
	private String content;
	@NotNull(message = "Posted timestamp cannot be null")
    @PastOrPresent(message = "Posted timestamp must be in the past or present")
    @NoSQLInjection(message = "Posted timestamp cannot contain SQL Injection patterns")
	private Timestamp posted;
	@NotBlank(message = "Username cannot be null or blank")
	@Size(min = 6, max = 15, message = "Username size must be between 6 and 15")
	@NoSQLInjection(message = "Username cannot contain SQL Injection patterns")
	private String username;

}

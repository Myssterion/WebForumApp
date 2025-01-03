package springboot.app.webforum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.core.AuthenticationException;

import springboot.app.webforum.exception.InvalidUsernameException;
import springboot.app.webforum.exception.InvalidVerificationCodeException;
import springboot.app.webforum.exception.InvalidUserRoleException;
import springboot.app.webforum.exception.InvalidEntityIdException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ InvalidUsernameException.class, InvalidVerificationCodeException.class,
			AuthenticationException.class })
	public ResponseEntity<Void> handleAuthenticationExceptions(RuntimeException ex) {
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidEntityIdException.class)
	public ResponseEntity<Void> handleInvalidAttributesExceptions(InvalidEntityIdException ex) {
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidUserRoleException.class)
	public ResponseEntity<Void> handleInvalidUserRoleExceptions(InvalidUserRoleException ex) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleServerExceptions(Exception ex) {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

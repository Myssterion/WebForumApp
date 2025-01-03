package springboot.app.webforum.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;

import springboot.app.webforum.dto.AuthRequest;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.dto.VerifyRequest;
import springboot.app.webforum.service.AuthService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthService authService;

	public ResponseEntity<?> login(AuthRequest authRequest) {
		authService.login(authRequest);
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> verify(VerifyRequest verifyRequest) {
		ResponseCookie jwt = authService.verify(verifyRequest);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt.toString()).build();
	}

	public ResponseEntity<?> register(UserDto registerRequest) {
		authService.register(registerRequest);
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> retreive(String temp) {
		ResponseCookie jwt = authService.retrieve(temp);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt.toString()).build();
	}

}

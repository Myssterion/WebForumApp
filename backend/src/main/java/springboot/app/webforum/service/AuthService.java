package springboot.app.webforum.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.config.SecurityConfig;
import springboot.app.webforum.dto.AuthRequest;
import springboot.app.webforum.dto.UserDto;
import springboot.app.webforum.dto.VerifyRequest;
import springboot.app.webforum.entity.Role;
import springboot.app.webforum.entity.Status;
import springboot.app.webforum.entity.User;
import springboot.app.webforum.exception.InvalidEntityAttributeException;
import springboot.app.webforum.exception.InvalidUserRoleException;
import springboot.app.webforum.exception.InvalidUsernameException;
import springboot.app.webforum.exception.InvalidVerificationCodeException;
import springboot.app.webforum.repository.RoleRepository;
import springboot.app.webforum.repository.StatusRepository;
import springboot.app.webforum.repository.UserRepository;
import springboot.app.webforum.util.VerificationCodeGenerator;

import static springboot.app.webforum.util.Constants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final EmailService emailService;
	private final JWTService jwtService;
	private final StatusRepository statusRepository;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authManager;
	private final JWTOauth2ListService jwtOauth2Service;

	public void login(AuthRequest authRequest) {
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		User user = userRepository.findByUsername(authRequest.getUsername())
				.orElseThrow(() -> new InvalidUsernameException("Invalid username or password!"));
		
		if(!user.getStatus().getStatusName().equals(APPROVED))
			throw new InvalidUserRoleException("User account has not been approved!");
		
		String code = VerificationCodeGenerator.generateCode();
		LocalDateTime localDateTime = LocalDateTime.now();
		localDateTime = localDateTime.plusMinutes(10);
		Timestamp expires = Timestamp.valueOf(localDateTime);
		user.setVerificationCode(code);
		user.setCodeExpiresAt(expires);
		userRepository.save(user);
		emailService.sendVerificationCode(user.getEmail(), code);
	}

	public ResponseCookie verify(VerifyRequest verifyRequest) {
		User user = userRepository.findByUsername(verifyRequest.getUsername())
				.orElseThrow(() -> new InvalidUsernameException("No user with given verification code"));
		String verificationCode = user.getVerificationCode();
		Timestamp expiresAt = user.getCodeExpiresAt();
		if (!verificationCode.equals(verifyRequest.getVerificationCode()) || expiresAt.before(Timestamp.valueOf(LocalDateTime.now())))
		 throw new InvalidVerificationCodeException("Verification code is incorrect or has expired!");

		String jwt = jwtService.generateToken(user);
		ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(false)
                .domain("localhost")
                .secure(true)  // Only send over HTTPS
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(3))
                .build();
		return cookie;
	}
	
	public ResponseCookie retrieve(String temp) {
		String jwt = jwtOauth2Service.removeJWT(temp);
		ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(false)
                .domain("localhost")
                .secure(true)  // Only send over HTTPS
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(3))
                .build();
		return cookie;
	}

	public void register(UserDto registerRequest) {
		Status status = statusRepository.findByStatusName(PENDING)
				.orElseThrow(() -> new InvalidEntityAttributeException("No Status with given name!"));

		Role role = roleRepository.findByRoleName(USER)
				.orElseThrow(() -> new InvalidEntityAttributeException("No Role with given name!"));

		PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();

		User user = User.builder().name(registerRequest.getName()).surname(registerRequest.getSurname())
				.username(registerRequest.getUsername()).email(registerRequest.getEmail())
				.password(passwordEncoder.encode(registerRequest.getPassword())).status(status).role(role)
				.registeredAt(Timestamp.valueOf(LocalDateTime.now())).build();

		userRepository.save(user);
	}

	public void oauth2Login(String email) {
		User user = userRepository.findByUsername(email).orElse(null);

		if (user == null) {
			Status status = statusRepository.findByStatusName(APPROVED)
					.orElseThrow(() -> new InvalidEntityAttributeException("No Status with given name!"));

			Role role = roleRepository.findByRoleName(USER)
					.orElseThrow(() -> new InvalidEntityAttributeException("No Role with given name!"));

			user = User.builder().username(email).email(email).status(status).role(role)
					.registeredAt(Timestamp.valueOf(LocalDateTime.now())).build();

			userRepository.save(user);
		}
	}
}

package springboot.app.webforum.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import springboot.app.webforum.entity.SecurityLog;
import springboot.app.webforum.entity.User;
import springboot.app.webforum.exception.InvalidEntityAttributeException;
import springboot.app.webforum.repository.SecurityLogRepository;
import springboot.app.webforum.repository.UserRepository;

@Service
@Transactional
@AllArgsConstructor
public class SecurityLogService {

	private final SecurityLogRepository securityLogRepository;
	private final UserRepository userRepository;

	public void addSecurityLog(String username, String httpMethod, String uri, String context) {	
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new InvalidEntityAttributeException("No user with given username!"));
		
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		SecurityLog securityLog = SecurityLog.builder()
											.user(user)
											.context(context)
											.httpMethod(httpMethod)
											.uri(uri)
											.loggedAt(timestamp)
											.build();
		
		securityLogRepository.save(securityLog);
	}

}

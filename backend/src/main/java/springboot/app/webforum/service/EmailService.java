package springboot.app.webforum.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import static springboot.app.webforum.util.Constants.*;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final String CODE_SUBJECT = "Webforum Verification Code";
	private final String CODE_TEXT = "Your verification code. Enter this code to finish the process: ";
	private final String REGISTER_SUBJECT = "Webforum Account Reviewed"; 
	private final String REGISTER_TEXT_APPROVED = "Your account has been reviewed and approved. You may login using your account on Webforum now!";
	private final String REGISTER_TEXT_BANNED = "Your account has been reviewd and wasn't approved. Please read our policies and try to register again!";
	
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Async
	public void sendVerificationCode(String to, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(CODE_SUBJECT);
		message.setText(CODE_TEXT + code);
		mailSender.send(message);
	}

	@Async
	public void sendAccountStatusMessage(String to, String statusName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(REGISTER_SUBJECT);
		if(statusName.equals(APPROVED))
			message.setText(REGISTER_TEXT_APPROVED);
		else if(statusName.equals(BANNED))
			message.setText(REGISTER_TEXT_BANNED);
		mailSender.send(message);
		
	}
}

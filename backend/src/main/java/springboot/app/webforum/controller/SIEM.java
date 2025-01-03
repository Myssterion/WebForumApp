package springboot.app.webforum.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.event.EventListener;

import lombok.RequiredArgsConstructor;
import springboot.app.webforum.service.SecurityLogService;
import springboot.app.webforum.util.MaliciousRequestEvent;

@Component
@RequiredArgsConstructor
public class SIEM {

	private final SecurityLogService securityLogService;

	@EventListener
	public void handleMaliciousRequestEvent(MaliciousRequestEvent event) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (attributes != null && authentication != null && authentication.isAuthenticated()) {
	            HttpServletRequest request = attributes.getRequest();

	            String httpMethod = request.getMethod();
	            String endpoint = request.getRequestURI();
	        	String username = authentication.getName(); 
	        	String context = event.getContext();
				securityLogService.addSecurityLog(username, httpMethod, endpoint, context);
		 }
	}

}

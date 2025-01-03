package springboot.app.webforum.config;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import springboot.app.webforum.entity.Role;
import springboot.app.webforum.entity.Status;
import springboot.app.webforum.entity.User;
import springboot.app.webforum.exception.InvalidEntityAttributeException;
import springboot.app.webforum.repository.RoleRepository;
import springboot.app.webforum.repository.StatusRepository;
import springboot.app.webforum.repository.UserRepository;
import springboot.app.webforum.service.JWTOauth2ListService;
import springboot.app.webforum.service.JWTService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.Authentication;  // Authentication object
import org.springframework.security.oauth2.core.user.OAuth2User;  // OAuth2User for extracting user attributes
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken; 

import static springboot.app.webforum.util.Constants.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final UserRepository userRepository;
	private final JWTService jwtService;
	private final StatusRepository statusRepository;
	private final RoleRepository roleRepository;
	private final JWTOauth2ListService jwtOauth2Service;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
    	 
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();
        
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        
        User user = oauth2Login(email);
        
        String jwt = jwtService.generateToken(user);
        
        String temporaryToken = UUID.randomUUID().toString();
        jwtOauth2Service.addJWT(temporaryToken, jwt);
        
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:4200/home?temp=" + temporaryToken);  
    }

  
    private User oauth2Login(String email) {
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
		return user;
	}
}
package springboot.app.webforum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.context.request.RequestContextListener;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import springboot.app.webforum.service.CustomUserDetailsService;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableAsync
public class SecurityConfig {

	private final CustomUserDetailsService userService;
	private final JWTAuthenticationFilter jwtAuthenticationFilter;
	 @Resource
	 private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	 @Resource
	 private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler; 


	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(
				httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/user/find/**", "/user/update/**").hasRole("ADMIN")
						.requestMatchers("/comment/find/new","/comment/update/**").hasAnyRole("ADMIN", "MODERATOR")
						.requestMatchers(HttpMethod.POST, "/auth/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/auth/retrieve/jwt", "/comment/find/topic/**").permitAll()
						.requestMatchers("/comment/find/topic/**", "/comment/post/new/**").hasAnyRole("ADMIN", "MODERATOR","USER")
						.anyRequest().permitAll()) 
				.oauth2Login((oauth2) -> oauth2
						.redirectionEndpoint(redirectionEndpoint -> 
	                    redirectionEndpoint
	                        .baseUri("/auth/login/oauth2"))
						.successHandler(customAuthenticationSuccessHandler)
						.failureHandler(customAuthenticationFailureHandler))
				.sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
				.authenticationProvider(daoAuthenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("https://localhost:4200")); 
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
																									
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Origin"));
		configuration.setAllowCredentials(true); 

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
	
	@Bean
    public SecurityContextHolder requestContextHolder() {
        return new SecurityContextHolder();
    }
}

package springboot.app.webforum.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import springboot.app.webforum.service.CustomUserDetailsService;
import springboot.app.webforum.service.JWTService;
import springboot.app.webforum.service.TokenBlacklistService;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private final JWTService jwtService;
	private final CustomUserDetailsService userService;
	private final TokenBlacklistService tokenBlacklistService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		try {

			final String username;
		
			String jwt = null;
			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if ("jwt".equals(cookie.getName())) {
						jwt = cookie.getValue();
						break;
					}
				}
			}
			
			if (jwt == null) {
				filterChain.doFilter(request, response);
				return;
			}
			
			username = jwtService.extractUserName(jwt);
			
			if (jwt != null && username != null && !username.isEmpty()
					&& SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userService.loadUserByUsername(username);
				if (jwtService.isTokenValid(jwt, userDetails)) {
					if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malicious Request Detected");
						return;
					}
					SecurityContext context = SecurityContextHolder.createEmptyContext();
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					context.setAuthentication(authToken);
					SecurityContextHolder.setContext(context);
				}
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
	}

}

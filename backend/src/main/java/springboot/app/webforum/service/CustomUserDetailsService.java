package springboot.app.webforum.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.app.webforum.entity.User;
import springboot.app.webforum.exception.InvalidUsernameException;
import springboot.app.webforum.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService{

	private UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new InvalidUsernameException("User with given username doesnt exist!")); 
		
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());
		
		if(user.getPassword() != null)
			return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),Collections.singleton(authority));
		else
			return new org.springframework.security.core.userdetails.User(user.getUsername(),"passsword",Collections.singleton(authority));
		
	}

}

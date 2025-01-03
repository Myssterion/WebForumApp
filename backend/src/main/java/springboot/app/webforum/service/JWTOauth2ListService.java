package springboot.app.webforum.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class JWTOauth2ListService {
	
	private Map<String, String> jwtMap = new HashMap<>();

	public void addJWT(String temp, String jwt) {
		jwtMap.put(temp, jwt);
	}
	
	public String removeJWT(String temp) {
		return jwtMap.get(temp);
	}

}

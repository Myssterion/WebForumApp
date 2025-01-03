package springboot.app.webforum.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);
	
	Optional<User> findByUsernameAndPassword(String username, String password);

	Optional<User> findByUsernameAndVerificationCode(String username, String verificationCode);

	List<User> findByStatusIdOrderByRegisteredAt(Integer statusId);

	List<User> findByRoleIdNot(Integer roleId);

	Optional<User> findByVerificationCode(String verificationCode);

}

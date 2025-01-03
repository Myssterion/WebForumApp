package springboot.app.webforum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.Status;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

	Optional<Status> findByStatusName(String string);

}

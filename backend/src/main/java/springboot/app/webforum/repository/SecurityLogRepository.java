package springboot.app.webforum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.SecurityLog;

@Repository
public interface SecurityLogRepository extends JpaRepository<SecurityLog, Integer> {

}

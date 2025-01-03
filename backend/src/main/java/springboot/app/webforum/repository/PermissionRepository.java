package springboot.app.webforum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.app.webforum.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

	Optional<Permission> findByPermissionName(String permissionName);

}

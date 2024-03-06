package finance.L21s.userdataimporter.repository;

import finance.L21s.userdataimporter.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleNameAndSystemUserId(String roleName, Integer systemUserId);
}

package finance.L21s.userdataimporter.repository;

import finance.L21s.userdataimporter.model.Role;
import finance.L21s.userdataimporter.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    void deleteAllBySystemUserIn(List<SystemUser> systemUsers);
}

package finance.L21s.userdataimporter.repository;

import finance.L21s.userdataimporter.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {
}

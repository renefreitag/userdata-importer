package finance.L21s.userdataimporter.service;

import finance.L21s.userdataimporter.model.SystemUser;
import finance.L21s.userdataimporter.repository.SystemUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;

    public List<SystemUser> getAllUsers() {
        return systemUserRepository.findAll();
    }

}

package finance.L21s.userdataimporter.controller;

import finance.L21s.userdataimporter.service.SystemUserService;
import finance.L21s.userdataimporter.model.SystemUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/systemuser")
@RequiredArgsConstructor
public class SystemUserController {

    private final SystemUserService systemUserService;

    @GetMapping
    public List<SystemUser> getAllUsers() {
        return systemUserService.getAllUsers();
    }

}

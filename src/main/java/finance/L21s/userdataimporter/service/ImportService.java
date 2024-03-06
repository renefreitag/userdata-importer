package finance.L21s.userdataimporter.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import finance.L21s.userdataimporter.model.Role;
import finance.L21s.userdataimporter.model.SystemUser;
import finance.L21s.userdataimporter.repository.RoleRepository;
import finance.L21s.userdataimporter.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImportService {


    private final SystemUserRepository systemUserRepository;
    private final RoleRepository roleRepository;


    public void importData(String usersCsvPath, String rolesCsvPath) throws IOException, CsvException {
        importUsers(usersCsvPath);
        importRoles(rolesCsvPath);
    }

    private void importUsers(String path) throws IOException, CsvException {
        List<SystemUser> existingUsers = systemUserRepository.findAll()
                .stream().toList();

        Map<String, SystemUser> existingUsersMap = existingUsers.stream()
                .collect(Collectors.toMap(u -> u.getId() + u.getEmail(), Function.identity()));

        List<SystemUser> newOrEditedUsersList = new ArrayList<>();
        List<Integer> allImportedUserIdsList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                Integer id = Integer.parseInt(line[0]);
                String email = line[1];
                SystemUser user = new SystemUser();
                user.setId(id);
                user.setEmail(email);
                if (!existingUsersMap.containsKey(id + email)) {
                    newOrEditedUsersList.add(user);
                }
                allImportedUserIdsList.add(user.getId());
            }
        }

        List<SystemUser> usersToDelete = existingUsers.stream()
                .filter(existingUser -> !allImportedUserIdsList.contains(existingUser.getId()))
                .collect(Collectors.toList());

        systemUserRepository.deleteAll(usersToDelete);
        roleRepository.deleteAllBySystemUserIn(usersToDelete);
        systemUserRepository.saveAll(newOrEditedUsersList);
    }

    private void importRoles(String path) throws IOException, CsvException {
        Map<Integer, SystemUser> existingUsersMap = systemUserRepository.findAll()
                .stream()
                .collect(Collectors.toMap(SystemUser::getId, Function.identity()));

        List<Role> existingRoles = roleRepository.findAll()
                .stream().toList();

        Map<String, Role> existingRolesMap = existingRoles.stream()
                .collect(Collectors.toMap(r -> r.getSystemUser().getId() + r.getRoleName(), Function.identity()));

        List<Role> newRolesList = new ArrayList<>();
        Set<String> allImportedRolesList = new HashSet<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                int userId = Integer.parseInt(line[0]);
                String roleName = line[1];

                Role role = new Role();
                role.setRoleName(roleName);
                role.setSystemUser(existingUsersMap.get(userId));
                if (role.getSystemUser() == null) {
                    continue;
                }
                if (!existingRolesMap.containsKey(userId + roleName)) {
                    newRolesList.add(role);
                }
                allImportedRolesList.add(role.getSystemUser().getId() + role.getRoleName());
            }
        }
        List<Role> rolesToDelete = existingRoles.stream()
                .filter(existingRole -> !allImportedRolesList.contains(existingRole.getSystemUser().getId() + existingRole.getRoleName()))
                .collect(Collectors.toList());

        roleRepository.saveAll(newRolesList);
        roleRepository.deleteAll(rolesToDelete);
    }
}

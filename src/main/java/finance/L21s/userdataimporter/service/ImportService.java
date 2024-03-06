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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImportService {

    private static final String USERS_CSV = "src/main/resources/sap_export/users.csv";
    private static final String ROLES_CSV = "src/main/resources/sap_export/roles.csv";

    private final SystemUserRepository systemUserRepository;
    private final RoleRepository roleRepository;


    public void importData() throws IOException, CsvException {
        List<SystemUser> existingUsers = systemUserRepository.findAll()
                .stream().toList();

        importUsers(existingUsers);
//        importRoles(existingUsers);
    }

    private void importUsers(List<SystemUser> existingUsers) throws IOException, CsvException {
        Map<String, SystemUser> existingUsersMap = existingUsers.stream()
                .collect(Collectors.toMap(u -> u.getId() + u.getEmail(), Function.identity()));

        List<SystemUser> newOrEditedUsersList = new ArrayList<>();
        List<SystemUser> allImportedUsersList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(USERS_CSV))) {
            // Skip the header row
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
                allImportedUsersList.add(user);
            }
        }

        List<SystemUser> usersToDelete = existingUsers.stream()
                .filter(u -> !allImportedUsersList.contains(u))
                .collect(Collectors.toList());

        systemUserRepository.deleteAll(usersToDelete);
        systemUserRepository.saveAll(newOrEditedUsersList);
    }

    private void importRoles(List<SystemUser> users) throws IOException, CsvException {
        List<Role> newRolesList = new ArrayList<>();
        List<Role> allImportedRolesList = new ArrayList<>();
        List<Role> existingRoles = roleRepository.findAll()
                .stream().toList();
        List<Role> rolesToDelete = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(ROLES_CSV))) {
            // Skip the header row
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                int userId = Integer.parseInt(line[0]);
                String roleName = line[1];

                Role role = new Role();
                role.setRoleName(roleName);
                role.setSystemUser(users.stream()
                        .filter(u -> u.getId().equals(userId))
                        .findFirst()
                        .orElse(null));
                if (role.getSystemUser() == null) {
                    rolesToDelete.add(role);
                    continue;
                }
                if(existingRoles.stream().noneMatch(r -> r.getRoleName().equals(roleName) && r.getSystemUser().getId().equals(userId))) {
                    newRolesList.add(role);
                }
                allImportedRolesList.add(role);
            }
        }
        // delete roles that are not in the csv file
        rolesToDelete.addAll(existingRoles.stream()
                .filter(r -> allImportedRolesList.stream()
                        .noneMatch(r2 -> r2.getRoleName().equals(r.getRoleName()) && r2.getSystemUser().getId().equals(r.getSystemUser().getId())))
                .toList());
        roleRepository.deleteAll(rolesToDelete);
        roleRepository.saveAll(newRolesList);
    }
}

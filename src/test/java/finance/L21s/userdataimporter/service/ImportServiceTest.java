package finance.L21s.userdataimporter.service;


import com.opencsv.exceptions.CsvException;
import finance.L21s.userdataimporter.model.Role;
import finance.L21s.userdataimporter.model.SystemUser;
import finance.L21s.userdataimporter.repository.RoleRepository;
import finance.L21s.userdataimporter.repository.SystemUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImportServiceTest {

    private static final String USERS_CSV = "src/test/resources/sap_export_test/users_test.csv";
    private static final String ROLES_CSV = "src/test/resources/sap_export_test/roles_test.csv";

    @Mock
    private SystemUserRepository systemUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ImportService importService;

    @Test
    public void importData_with_Different_DB_and_CSV_Data() throws IOException, CsvException {
        when(systemUserRepository.findAll()).thenReturn(getMockSystemUsersFromDatabase());
        when(roleRepository.findAll()).thenReturn(getMockRolesFromDatabase());

        // Capture the arguments passed to saveAll
        ArgumentCaptor<List<SystemUser>> userSaveCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<SystemUser>> userDeleteCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Role>> roleSaveCaptor = ArgumentCaptor.forClass(List.class);

        importService.importData(USERS_CSV, ROLES_CSV);

        // assertions
        verify(systemUserRepository, times(1)).deleteAll(userDeleteCaptor.capture());
        verify(roleRepository, times(1)).deleteAllBySystemUserIn(any());
        verify(systemUserRepository, times(1)).saveAll(userSaveCaptor.capture());
        verify(roleRepository, times(1)).deleteAll(any());
        verify(roleRepository, times(1)).saveAll(roleSaveCaptor.capture());

        List<SystemUser> actualUsersSave = userSaveCaptor.getValue();
        List<SystemUser> actualUsersDelete = userDeleteCaptor.getValue();

        List<SystemUser> expectedUsersFromSave = getExpectedUsersFromSave();
        List<SystemUser> expectedUsersFromDelete = getExpectedUsersFromDelete();

        assertEquals(expectedUsersFromSave.get(0).getId(), actualUsersSave.get(0).getId());
        assertEquals(expectedUsersFromDelete.get(0).getId(), actualUsersDelete.get(0).getId());
    }

    private List<SystemUser> getMockSystemUsersFromDatabase() {
        return List.of(
                SystemUser.builder().id(1).email("mail1").build(),
                SystemUser.builder().id(2).email("mail2").build());
    }

    private List<Role> getMockRolesFromDatabase() {
        return List.of(
                Role.builder().id(1).roleName("read").systemUser(getMockSystemUsersFromDatabase().get(0)).build(),
                Role.builder().id(2).roleName("write").systemUser(getMockSystemUsersFromDatabase().get(1)).build());
    }

    private List<SystemUser> getExpectedUsersFromSave() {
        return List.of(
                SystemUser.builder().id(3).email("mail3").build());
    }


    private List<SystemUser> getExpectedUsersFromDelete() {
        return List.of(
                SystemUser.builder().id(2).email("mail2").build());
    }

}

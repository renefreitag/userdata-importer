package finance.L21s.userdataimporter.service;

import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImportJobService {

    private static final String USERS_CSV = "src/main/resources/sap_export/users.csv";
    private static final String ROLES_CSV = "src/main/resources/sap_export/roles.csv";
    private final ImportService importService;

    @Scheduled(cron = "0 2 1 * * *")
    public void startImportJob() throws IOException, CsvException {
        importService.importData(USERS_CSV, ROLES_CSV);
    }

}

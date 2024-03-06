package finance.L21s.userdataimporter.service;

import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImportJobService {

    private final ImportService importService;

    @Scheduled(cron = "0 2 1 * * *")
    public void startImportJob() throws IOException, CsvException {
        importService.importData();
    }

}

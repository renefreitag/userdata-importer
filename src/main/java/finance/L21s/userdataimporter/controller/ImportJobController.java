package finance.L21s.userdataimporter.controller;

import com.opencsv.exceptions.CsvException;
import finance.L21s.userdataimporter.service.ImportJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/importjob")
@RequiredArgsConstructor
public class ImportJobController {

    private final ImportJobService importJobService;

    @PostMapping
    public void startImportJob() throws IOException, CsvException {
        importJobService.startImportJob();
    }
}

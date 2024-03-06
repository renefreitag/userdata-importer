package finance.L21s.userdataimporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UserdataImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserdataImporterApplication.class, args);
	}

}

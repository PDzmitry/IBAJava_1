package by.protasovitski.calendariba;


import by.protasovitski.calendariba.report.ReportsProperties;
import by.protasovitski.calendariba.report.storage.StorageService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(ReportsProperties.class)
@SpringBootApplication()
public class CalendaribaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendaribaApplication.class, args);
    }

    @Bean
    ApplicationRunner init(StorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}

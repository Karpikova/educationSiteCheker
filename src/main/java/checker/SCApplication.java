package checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SCApplication {

    public static void main(String[] args) {
        SpringApplication.run(SCApplication.class, args);
    }

}


package ua.tucha.passpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ua.tucha.passpass.core", "ua.tucha.passpass.web"})
public class PassPassApplication {
	public static void main(String[] args) {
	    SpringApplication.run(PassPassApplication.class, args);
	}
}


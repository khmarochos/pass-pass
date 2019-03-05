package ua.tucha.passpass.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@SpringBootConfiguration
@PropertySource(value = { "classpath:application.properties" })
public class GlobalConfiguration {

}

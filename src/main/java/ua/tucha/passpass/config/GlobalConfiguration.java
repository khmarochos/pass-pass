package ua.tucha.passpass.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@SpringBootConfiguration
@PropertySource(value = { "classpath:application.properties" })
public class GlobalConfiguration {

//    @Bean(name="globalMessageSource")
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("classpath:messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

//    @Bean
//    public LocalValidatorFactoryBean getValidator() {
//        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
//        localValidatorFactoryBean.setValidationMessageSource(messageSource());
//        log.debug("LocalValidatorFactoryBean = {}, ReloadableResourceBundleMessageSource = {}",
//                localValidatorFactoryBean,
//                messageSource()
//        );
//        return localValidatorFactoryBean;
//    }

}

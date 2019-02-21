package ua.tucha.passpass.core.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Slf4j
@SpringBootConfiguration
@ComponentScan(basePackages = "ua.tucha.passpass.core")
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {

    private final PropertyResolver environment;

    @Autowired
    public AppConfig(PropertyResolver environment) {
        this.environment = environment;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    @Autowired
    @Lazy(false)
    public HibernatePBEStringEncryptor hibernateStringEncryptor(PBEStringEncryptor strongEncryptor) {
        HibernatePBEStringEncryptor hibernateEncryptor = new HibernatePBEStringEncryptor();
        hibernateEncryptor.setRegisteredName("STRING_ENCRYPTOR");
        hibernateEncryptor.setEncryptor(strongEncryptor);
        return hibernateEncryptor;
    }

    @Bean
    @Lazy(false)
    public PBEStringEncryptor strongEncryptor() {
        String encryptorKey = environment.getRequiredProperty("encryptorKey");
        PooledPBEStringEncryptor strongEncryptor = new PooledPBEStringEncryptor();
        strongEncryptor.setPassword(encryptorKey);
        strongEncryptor.setAlgorithm("PBEWithMD5AndDES");
        strongEncryptor.setPoolSize(4);

        return strongEncryptor;
    }

}

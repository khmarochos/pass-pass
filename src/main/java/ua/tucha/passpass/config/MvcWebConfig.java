package ua.tucha.passpass.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.http.CacheControl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ua.tucha.passpass")
public class MvcWebConfig implements WebMvcConfigurer {

    @Autowired
    private PropertyResolver environment;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    @Autowired
    @Lazy(false)
    public HibernatePBEStringEncryptor hibernateStringEncryptor(PBEStringEncryptor strongEncryptor){
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

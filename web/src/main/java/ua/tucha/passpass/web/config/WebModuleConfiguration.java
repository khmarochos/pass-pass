package ua.tucha.passpass.web.config;

import lombok.extern.slf4j.Slf4j;
// import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.CacheControl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.tucha.passpass.web.security.UserDetailsFactory;
import ua.tucha.passpass.web.util.FrontendMessageInterceptor;
// import ua.tucha.passpass.web.util.FrontendMessageInterceptorFactory;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "ua.tucha.passpass.web")
public class WebModuleConfiguration implements WebMvcConfigurer {

    private final PropertyResolver environment;
    private final MessageSource messageSource;
    private final LocalValidatorFactoryBean localValidatorFactoryBean;
    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
    private final FrontendMessageInterceptor frontendMessageInterceptor;

    @Autowired
    public WebModuleConfiguration(
            PropertyResolver environment,
            MessageSource messageSource,
            @Qualifier("coreLocalValidatorFactoryBean") LocalValidatorFactoryBean localValidatorFactoryBean,
            AutowireCapableBeanFactory autowireCapableBeanFactory,
            FrontendMessageInterceptor frontendMessageInterceptor
    ) {
        this.environment = environment;
        this.messageSource = messageSource;
        this.localValidatorFactoryBean = localValidatorFactoryBean;
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
        this.frontendMessageInterceptor = frontendMessageInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean(name = "layoutDialect")
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Bean(name = "webLocalValidatorFactoryBean")
    public LocalValidatorFactoryBean getValidator() {
        return this.localValidatorFactoryBean;
    }

    @Bean(name = "userDetails")
    public UserDetailsFactory userDetailsFactory() {
        return new UserDetailsFactory(autowireCapableBeanFactory);
    }

    // @Bean(name = "frontendMessageInterceptor")
    // FrontendMessageInterceptorFactory frontendMessageInterceptorFactory() {
    //     return new FrontendMessageInterceptorFactory(autowireCapableBeanFactory);
    // }

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     try {
    //         registry.addInterceptor(frontendMessageInterceptorFactory().getObject());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(frontendMessageInterceptor);
    }
}

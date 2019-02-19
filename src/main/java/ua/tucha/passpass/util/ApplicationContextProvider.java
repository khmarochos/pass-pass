package ua.tucha.passpass.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ApplicationContextProvider.applicationContext = context;
    }

    public static Object getBean(Class clazz) {
        return ApplicationContextProvider.applicationContext.getBean(clazz);
    }

    public static Object getBean(String qualifier, Class clazz) {
        return ApplicationContextProvider.applicationContext.getBean(qualifier, clazz);
    }
}

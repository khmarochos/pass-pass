// package ua.tucha.passpass.web.util;
//
// import org.springframework.beans.factory.FactoryBean;
// import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
//
// public class FrontendMessageInterceptorFactory implements FactoryBean<FrontendMessageInterceptor> {
//
//     private AutowireCapableBeanFactory beanFactory;
//
//     public FrontendMessageInterceptorFactory(AutowireCapableBeanFactory beanFactory) {
//         this.beanFactory = beanFactory;
//     }
//
//     @Override
//     public FrontendMessageInterceptor getObject() throws Exception {
//         FrontendMessageInterceptor frontendMessageInterceptor = new FrontendMessageInterceptor();
//         beanFactory.autowireBean(frontendMessageInterceptor);
//         return frontendMessageInterceptor;
//     }
//
//     @Override
//     public Class<?> getObjectType() {
//         return FrontendMessageInterceptor.class;
//     }
//
//     @Override
//     public boolean isSingleton() {
//         return true;
//     }
// }

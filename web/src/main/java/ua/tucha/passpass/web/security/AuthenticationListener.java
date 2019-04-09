// package ua.tucha.passpass.web.security;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.ApplicationListener;
// import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
// import org.springframework.stereotype.Component;
// import ua.tucha.passpass.web.model.FrontendMessage;
// import ua.tucha.passpass.web.service.FrontendMessageStackService;
//
// @Component
// public class AuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent> {
//
//     @Autowired
//     FrontendMessageStackService frontendMessageStackService;
//
//     @Override
//     public void onApplicationEvent(AuthenticationSuccessEvent event) {
//         frontendMessageStackService.pushFrontendMessage(
//                 new FrontendMessage(
//                         FrontendMessage.MessageType.SUCCESS,
//                         "Welcome!",
//                         "Authentication completed"
//                         // FIXME: use message.properties!
//                 )
//         );
//     }
// }

package ua.tucha.passpass.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.model.VerificationToken;
import ua.tucha.passpass.repository.VerificationTokenRepository;
import ua.tucha.passpass.util.RouteRegistry;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    private static final int TOKEN_EXPIRATION_TIME = 60 * 24;

    @Autowired
    public VerificationTokenService(
            VerificationTokenRepository verificationTokenRepository,
            UserService userService,
            MessageSource messages,
            JavaMailSender mailSender
    ) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userService = userService;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    // Here be the constructor methods

    public VerificationToken createVerificationToken(User user) {
        String uniqueIdentifier = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(uniqueIdentifier);
        verificationToken.setExpiry(calculateExpiryDate());
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    // Here be public methods

    public void sendVerificationToken(@NotNull User user, VerificationToken verificationToken, @NotNull Locale locale) {

        if(verificationToken == null) verificationToken = createVerificationToken(user);
        String confirmationURL = RouteRegistry.UserRouteRegistry.CONFIRM_EMAIL + "/" + verificationToken.getToken();
        String token = verificationToken.getToken();
        String[] messageArgs = new String[]{ confirmationURL, token };
        String messageRecipient = user.getEmail();
        String messageSubject = messages.getMessage("registration.verification.message.subject", null, locale);
        String messageBody = messages.getMessage("registration.verification.message.body", messageArgs, locale);;
        // TODO: use Thymeleaf!

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(messageRecipient);
        email.setSubject(messageSubject);
        email.setText(messageBody);
        mailSender.send(email);
    }

    public void sendVerificationToken(@NotNull User user, @NotNull Locale locale) {
        sendVerificationToken(user, null, locale);
    }

    public boolean checkVerificationToken(@NotNull VerificationToken verificationToken) {
        Date currentDate = currentDate();
        Date expiry = verificationToken.getExpiry();
        User user = verificationToken.getUser();
        return
                expiry != null && currentDate.before(expiry) &&
                user != null && user.getVerified() == null;
    }

    public boolean checkVerificationToken(@NotNull String verificationTokenString) {
        VerificationToken verificationToken = findVerificationTokenByToken(verificationTokenString);
        return (verificationToken != null) && checkVerificationToken(verificationToken);
    }

    public boolean applyVerificationToken(@NotNull VerificationToken verificationToken) {
        if(!checkVerificationToken(verificationToken)) { return false; }
        Date currentDate = currentDate();
        User user = verificationToken.getUser();
        user.setVerified(currentDate);
        userService.updateUser(user);
        return true;
    }

    public boolean applyVerificationToken(@NotNull String verificationTokenString) {
        VerificationToken verificationToken = findVerificationTokenByToken(verificationTokenString);
        return (verificationToken != null) && applyVerificationToken(verificationToken);
    }

    // Here be private methods

    private VerificationToken findVerificationTokenByToken(@NotNull String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

    private Date calculateExpiryDate(int expiryTime) {
        Date currentDate = currentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MINUTE, expiryTime);
        return new Date(calendar.getTime().getTime());
    }

    private Date calculateExpiryDate() {
        return calculateExpiryDate(TOKEN_EXPIRATION_TIME);
    }

    private Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

}

package ua.tucha.passpass.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.core.model.User;
import ua.tucha.passpass.core.model.VerificationToken;
import ua.tucha.passpass.core.repository.VerificationTokenRepository;
import ua.tucha.passpass.core.service.exception.VerificationTokenExpiredException;
import ua.tucha.passpass.core.service.exception.VerificationTokenNotFoundException;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    private static final int TOKEN_EXPIRATION_TIME = 60 * 24;

    @Autowired
    public VerificationTokenService(
            VerificationTokenRepository verificationTokenRepository,
            @Qualifier("messageSource") MessageSource messages,
            JavaMailSender mailSender
    ) {
        this.verificationTokenRepository = verificationTokenRepository;
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

    public void sendVerificationTokenToConfirmEmail(
            VerificationToken verificationToken,
            @NotNull User user,
            @NotNull String appURL,
            @NotNull Locale locale
    ) {

        String tokenString =
                verificationToken.getToken();
        // String confirmationURL = RouteRegistry.UserRouteRegistry.CONFIRM_EMAIL + "/" + verificationToken.getToken();
        String confirmationURL =
                appURL + "/" + tokenString;
        String[] messageArgs =
                new String[]{confirmationURL, tokenString};
        String messageRecipient =
                user.getEmail();
        String messageSubject =
                messages.getMessage(
                        "core.service.VerificationTokenService.sendVerificationTokenToConfirmEmail.message.subject",
                        null,
                        locale
                );
        String messageBody =
                messages.getMessage(
                        "core.service.VerificationTokenService.sendVerificationTokenToConfirmEmail.message.body",
                        messageArgs,
                        locale
                );
        ;
        // TODO: use Thymeleaf!

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(messageRecipient);
        email.setSubject(messageSubject);
        email.setText(messageBody);
        mailSender.send(email);
    }

    public void sendVerificationTokenToConfirmEmail(
            @NotNull User user,
            @NotNull String appURL,
            @NotNull Locale locale
    ) {
        VerificationToken verificationToken = createVerificationToken(user);
        sendVerificationTokenToConfirmEmail(verificationToken, user, appURL, locale);
    }


    boolean checkVerificationToken(@NotNull VerificationToken verificationToken) {
        Date expiry = verificationToken.getExpiry();
        return expiry != null && currentDate().before(expiry);
    }

    boolean checkVerificationToken(@NotNull String verificationTokenString) {
        VerificationToken verificationToken = findVerificationTokenByToken(verificationTokenString);
        return verificationToken != null && checkVerificationToken(verificationToken);
    }


    boolean applyVerificationToken(
            @NotNull VerificationToken verificationToken,
            boolean expireToken,
            boolean raiseException
    ) throws VerificationTokenExpiredException {
        // Checking the token
        if (!checkVerificationToken(verificationToken)) {
            if (raiseException) {
                throw new VerificationTokenExpiredException("Verification token "
                        + verificationToken.getToken()
                        + " is expired"
                );
            } else {
                return false;
            }
        }
        // Disabling the token if needed
        if (expireToken) {
            verificationToken.setExpiry(currentDate());
            verificationTokenRepository.save(verificationToken);
        }
        return true;
    }

    public boolean applyVerificationToken(
            @NotNull String verificationTokenString,
            boolean expireToken,
            boolean raiseException
    ) throws VerificationTokenNotFoundException, VerificationTokenExpiredException {
        VerificationToken verificationToken = findVerificationTokenByToken(verificationTokenString);
        if (verificationToken == null) {
            if (raiseException) {
                throw new VerificationTokenNotFoundException("No such verification token: " + verificationTokenString);
            } else {
                return false;
            }
        }
        return applyVerificationToken(verificationToken, expireToken, raiseException);
    }


    // Here be private methods

    VerificationToken findVerificationTokenByToken(@NotNull String verificationToken) {
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

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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private MessageSource messages;
    private JavaMailSender mailSender;

    private static final int TOKEN_EXPIRATION = 60 * 24;

    @Autowired
    public VerificationTokenService(
            VerificationTokenRepository verificationTokenRepository,
            MessageSource messages,
            JavaMailSender mailSender
    ) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    public VerificationToken createVerificationToken(User user) {
        String uniqueIdentifier = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(uniqueIdentifier);
        verificationToken.setExpiry(calculateExpiryDate());
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    private Date calculateExpiryDate(int expiryTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, expiryTime);
        return new Date(calendar.getTime().getTime());
    }

    private Date calculateExpiryDate() {
        return calculateExpiryDate(TOKEN_EXPIRATION);
    }

    public void sendVerificationToken(User user, VerificationToken verificationToken, Locale locale) {

        if(verificationToken == null) verificationToken = createVerificationToken(user);
        String confirmationURL = RouteRegistry.UserRouteRegistry.CONFIRM_EMAIL + "/" + verificationToken.getToken();
        String token = verificationToken.getToken();
        String[] messageArgs = new String[]{ confirmationURL, token };
        String messageRecipient = user.getEmail();
        String messageSubject = messages.getMessage("registration.verification.message.subject", null, locale);
        String messageBody = messages.getMessage("registration.verification.message.body", messageArgs, locale);;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(messageRecipient);
        email.setSubject(messageSubject);
        email.setText(messageBody);
        mailSender.send(email);
    }


}

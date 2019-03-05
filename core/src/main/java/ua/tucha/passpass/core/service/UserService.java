package ua.tucha.passpass.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.core.model.User;
import ua.tucha.passpass.core.model.VerificationToken;
import ua.tucha.passpass.core.model.VerificationTokenPurpose;
import ua.tucha.passpass.core.repository.UserRepository;
import ua.tucha.passpass.core.service.exception.EmailNotUniqueException;
import ua.tucha.passpass.core.service.exception.VerificationTokenExpiredException;
import ua.tucha.passpass.core.service.exception.VerificationTokenMispurposedException;
import ua.tucha.passpass.core.service.exception.VerificationTokenNotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    // repositories
    private final UserRepository userRepository;

    // services
    private final VerificationTokenService verificationTokenService;


    // the constructor
    @Autowired
    public UserService(
            UserRepository userRepository,
            VerificationTokenService verificationTokenService
    ) {
        this.userRepository = userRepository;
        this.verificationTokenService = verificationTokenService;
    }


    public User getUser(@NotNull long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isEmpty() ? null : user.get();
    }

    public User createUser(User user) throws EmailNotUniqueException {
        if (emailExists(user.getEmail())) {
            throw new EmailNotUniqueException();
        }
        Date currentDate = new Date();
        user.setCreated(currentDate);
        updateUser(user);
        return user;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }


    public User applyVerificationToken(
            VerificationToken verificationToken,
            VerificationTokenPurpose.Purpose purpose
    ) throws
            VerificationTokenExpiredException,
            VerificationTokenMispurposedException
    {
        verificationTokenService.applyVerificationToken(
                verificationToken,
                purpose,
                true,
                true
        );
        return verificationToken.getUser();
    }

    public User applyVerificationToken(
            String verificationToken,
            VerificationTokenPurpose.Purpose purpose
    ) throws
            VerificationTokenNotFoundException,
            VerificationTokenExpiredException,
            VerificationTokenMispurposedException
    {
        verificationTokenService.applyVerificationToken(
                verificationToken,
                purpose,
                true,
                true
        );
        return verificationTokenService.findVerificationTokenByToken(verificationToken).getUser();
    }


    public void verifyEmailByToken(String verificationToken) throws
            VerificationTokenNotFoundException,
            VerificationTokenExpiredException,
            VerificationTokenMispurposedException
    {
        User user = applyVerificationToken(verificationToken, VerificationTokenPurpose.Purpose.EMAIL_CONFIRMATION);
        if(user != null) {
            user.setVerified(currentDate());
            userRepository.save(user);
        }
    }


    public User findUserByVerificationToken(VerificationToken verificationToken) {
        return verificationToken.getUser();
    }

    public User findUserByVerificationToken(String verificationTokenString) {
        User user = null;
        VerificationToken verificationToken =
                verificationTokenService.findVerificationTokenByToken(verificationTokenString);
        if (verificationToken != null) {
            user = findUserByVerificationToken(verificationToken);
        }
        return user;
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }


    private java.sql.Date currentDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }


}

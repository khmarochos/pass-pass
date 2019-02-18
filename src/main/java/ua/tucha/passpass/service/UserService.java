package ua.tucha.passpass.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.repository.UserRepository;
import ua.tucha.passpass.service.exception.EmailNotUniqueException;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    // repository
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(@NotNull long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isEmpty() ? null : user.get();
    }

    public User createUser(User user) throws EmailNotUniqueException {
        if(emailExists(user.getEmail())) {
            throw new EmailNotUniqueException();
        }
        Date currentDate = new Date();
        user.setCreated(currentDate);
        user.setEnabled(false);
        userRepository.save(user);
        return user;
    }

    public boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

}

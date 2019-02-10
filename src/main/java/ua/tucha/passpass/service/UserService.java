package ua.tucha.passpass.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.repository.UserRepository;

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

    public User createNewUserAccount(User user) {
        Date currentDate = new Date();
        user.setCreated(currentDate);
        userRepository.save(user);
        return user;
    }

}

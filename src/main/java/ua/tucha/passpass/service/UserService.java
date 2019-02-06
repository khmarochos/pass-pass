package ua.tucha.passpass.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    // repository
    @Autowired
    private UserRepository userRepository;

    public User getUser(@NotNull long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isEmpty() ? null : user.get();
    }

    public User newUser() {
        return new User();
    }

}

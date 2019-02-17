package ua.tucha.passpass.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.model.VerificationToken;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}

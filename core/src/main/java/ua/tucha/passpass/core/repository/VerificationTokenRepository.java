package ua.tucha.passpass.core.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.core.model.VerificationToken;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}

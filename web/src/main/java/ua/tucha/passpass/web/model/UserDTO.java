package ua.tucha.passpass.web.model;

import lombok.Data;
import ua.tucha.passpass.core.model.validator.ValidEmail;
import ua.tucha.passpass.core.model.validator.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

    public interface CreateUserValidaionGroup { }

    @NotNull(groups = {CreateUserValidaionGroup.class})
    @NotEmpty(groups = {CreateUserValidaionGroup.class})
    private String name;

    @NotNull(groups = {CreateUserValidaionGroup.class})
    @ValidEmail(groups = {CreateUserValidaionGroup.class})
    private String email;

    @NotNull(groups = {CreateUserValidaionGroup.class})
    @ValidPassword(groups = {CreateUserValidaionGroup.class})
    private String password;

    @NotNull(groups = {CreateUserValidaionGroup.class})
    private boolean policyAccepted;
}

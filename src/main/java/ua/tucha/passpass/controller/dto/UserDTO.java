package ua.tucha.passpass.controller.dto;

import lombok.Data;
import ua.tucha.passpass.model.validator.ValidEmail;
import ua.tucha.passpass.model.validator.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

    public interface CreateUserGroup { }

    @NotNull(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    private String name;

    @NotNull(groups = {CreateUserGroup.class})
    @ValidEmail(groups = {CreateUserGroup.class})
    private String email;

    @NotNull(groups = {CreateUserGroup.class})
    @ValidPassword(groups = {CreateUserGroup.class})
    private String password;

    @NotNull(groups = {CreateUserGroup.class})
    private boolean policyAccepted;
}

package ua.tucha.passpass.web.model;

import lombok.Data;
import ua.tucha.passpass.core.model.validator.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EmailDTO {
    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;
}

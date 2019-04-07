package ua.tucha.passpass.web.model.formdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class VerificationTokenDTO {
    @NotNull
    @NotEmpty
    private String verificationToken;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean tokenApplied;
}

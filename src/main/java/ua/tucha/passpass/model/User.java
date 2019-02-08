package ua.tucha.passpass.model;

import lombok.*;
import ua.tucha.passpass.model.validator.ValidEmail;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table
@Entity
@Data
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public interface UserValidationGroup { }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @NotNull(groups = {UserValidationGroup.class})
    @NotEmpty(groups = {UserValidationGroup.class})
    private String name;

    @ValidEmail(groups = {UserValidationGroup.class})
    @NotNull(groups = {UserValidationGroup.class})
    @NotEmpty(groups = {UserValidationGroup.class})
    private String email;

    @NotNull(groups = {UserValidationGroup.class})
    @NotEmpty(groups = {UserValidationGroup.class})
    private String password;

    @NotNull
    private Date created;

    private Date disabled;

    private Date removed;

}

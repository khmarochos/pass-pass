package ua.tucha.passpass.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate5.type.EncryptedStringType;
import ua.tucha.passpass.model.validator.ValidEmail;
import ua.tucha.passpass.model.validator.ValidPassword;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@TypeDefs
        ({
                @TypeDef(
                        name="encryptedString",
                        typeClass= EncryptedStringType.class,
                        parameters={
                                @Parameter(name = "encryptorRegisteredName", value = "STRING_ENCRYPTOR")
                        }
                )
        })

@Table
@Entity
@Data
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public interface CreateUserGroup { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    private String name;

    @ValidEmail(groups = {CreateUserGroup.class})
    @NotNull(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    @Column(unique=true)
    private String email;

    @ValidPassword(groups = {CreateUserGroup.class})
    @NotNull(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    @Type(type="encryptedString")
    private String password;

    @NotNull
    private Date created;

    private Date disabled;

    private Date removed;

    @NotNull
    private boolean policyAccepted;

    @NotNull
    private boolean enabled;

    //@OneToMany(mappedBy = "user")
    //private List<VerificationToken> verificationTokenList;
}

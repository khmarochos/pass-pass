package ua.tucha.passpass.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.tucha.passpass.core.model.validator.ValidEmail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

// A type for enabling password encryption mechanism
// @TypeDefs
//         ({
//                 @TypeDef(
//                         name="encryptedString",
//                         typeClass= EncryptedStringType.class,
//                         parameters={
//                                 @Parameter(name = "encryptorRegisteredName", value = "STRING_ENCRYPTOR")
//                         }
//                 )
//         })

@Table
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    // Table fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @ValidEmail
    @NotNull
    @Column(unique=true)
    private String email;

    @NotNull
    // @Type(type="encryptedString")
    private String password;

    @NotNull
    private Date created;

    private Date disabled;

    private Date removed;

    @NotNull
    private boolean policyAccepted;

    private Date verified;

    // Foreign keys without table fields

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<VerificationToken> verificationTokenList;

}

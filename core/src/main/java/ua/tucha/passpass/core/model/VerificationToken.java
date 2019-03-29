package ua.tucha.passpass.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tucha.passpass.core.constant.VerificationTokenPurpose;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "owner",
            referencedColumnName = "id"
    )
    private User owner;

    private Date expiry;

    private VerificationTokenPurpose.Purpose verificationTokenPurpose;

}

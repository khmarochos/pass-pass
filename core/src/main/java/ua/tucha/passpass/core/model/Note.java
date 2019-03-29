package ua.tucha.passpass.core.model;

import lombok.*;

import javax.persistence.*;
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
public class Note {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotNull
    private String subject;

    @NotNull
    private String content;

    @ToString.Exclude
    @NotNull
    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "sender",
            referencedColumnName = "id"
    )
    private User sender;

    @ToString.Exclude
    @NotNull
    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "recipient",
            referencedColumnName = "id"
    )
    private User recipient;

    @NotNull
    private boolean encrypted;

    @NotNull
    private Date created;

    private Date expires;

    private Date removed;

    @NotNull
    private Date sent;

    private Date received;

}

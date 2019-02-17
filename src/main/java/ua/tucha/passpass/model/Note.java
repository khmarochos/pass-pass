package ua.tucha.passpass.model;

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

    @NotNull
    private long sender;

    @NotNull
    private long recipient;

    @NotNull
    private boolean encrypted;

    @NotNull
    private Date created;

    private Date expires;

    private Date removed;

    private Date sent;

    private Date received;

}

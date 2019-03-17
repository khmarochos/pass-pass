package ua.tucha.passpass.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRole {

    // public UserRole() {}

    // Table fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotNull
    @NotEmpty
    private String name;

    // Foreign keys without table fields

    @ManyToMany(mappedBy = "userRoleList")
    private List<User> userList;

    @ManyToMany
    @JoinTable(name = "user_role_x_user_privilege",
            joinColumns = @JoinColumn(name = "user_role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_privilege_id", referencedColumnName = "id"))
    private List<UserPrivilege> userPrivilegeList;

}

package ua.tucha.passpass.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class UserPrivilege {

        public UserPrivilege(@NotNull @NotEmpty String name) {
            this.name = name;
        }

        // Table fields

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @NotNull
        @NotEmpty
        private String name;

        // Foreign keys without table fields

        @ManyToMany(mappedBy = "userPrivilegeList")
        private List<UserRole> userRoleList;

}

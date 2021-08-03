package studio.dboo.favores.modules.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import studio.dboo.favores.modules.accounts.entity.AccountGroups;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Groups {

    @Id @GeneratedValue
    private Long id;

    /**Account Mapping*/
    @OneToMany(mappedBy = "account")
    private Set<AccountGroups> account = new HashSet<>();
}

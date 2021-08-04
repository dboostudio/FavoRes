package studio.dboo.favores.modules.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import studio.dboo.favores.modules.accounts.entity.Account;
import studio.dboo.favores.modules.accounts.entity.AccountGroups;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Groups {

    @Id @GeneratedValue
    private Long id;

    /** Account Mapping */
    @OneToMany(mappedBy = "account")
    private Set<AccountGroups> account = new HashSet<>();

    /** Group Info */
    private String groupLeaderUsername;                                     //그룹장
    @NotNull(message = "그룹명은 필수 입력 사항입니다.") @Column(unique = true)
    private String groupName;                                               //그룹명

    private String region;                                                  //지역
    private String category;                                                //카테고리(지역별맛집, 특정음식맛집, 테마맛집, etc.)
    private String targetFood;                                              //타겟음식

}

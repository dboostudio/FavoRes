package studio.dboo.favores.modules.accounts.entity;

import studio.dboo.favores.modules.groups.entity.Groups;

import javax.persistence.*;

@Entity
public class AccountGroups {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="groups_id")
    private Groups groups;
}

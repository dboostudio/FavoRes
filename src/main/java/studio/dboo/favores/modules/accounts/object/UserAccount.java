package studio.dboo.favores.modules.accounts.object;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import studio.dboo.favores.modules.accounts.entity.Account;

import java.util.List;

public class UserAccount extends User {
    private Account account;

    public UserAccount(Account account){
        super(account.getUsername(),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;

    }
}

package studio.dboo.favores.modules.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import studio.dboo.favores.modules.accounts.entity.Account;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    /** Bean */
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /** Constant */
    private static final String CANNOT_FIND_USER = "해당 유저명으로 가입된 계정이 없습니다.";
    private static final String ALREADY_EXIST_USER = "이미 해당 유저명으로 가입된 계정이 있습니다.";

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Account account = this.getAccount(usernameOrEmail);

        if(account == null){
            throw new UsernameNotFoundException(usernameOrEmail);
        }

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account createAccount(Account account){
        if(accountRepository.existsAccountByUsername(account.getUsername()) == true){
            throw new RuntimeException(ALREADY_EXIST_USER);
        }
        account.encodePassword(passwordEncoder);
        account.setRole("USER");
        return accountRepository.save(account);
    }

    public Account getAccount(String usernameOrEmail){
        Account account = accountRepository.findByUsername(usernameOrEmail);
        if(account == null){
            account = accountRepository.findByEmail(usernameOrEmail);
        }

        if(account == null){
            throw new UsernameNotFoundException(usernameOrEmail);
        }

        return account;
    }

    public Account updateAccount(Account account) {
        if(accountRepository.existsAccountByUsername(account.getUsername()) == false){
            throw new RuntimeException(CANNOT_FIND_USER);
        }
        accountRepository.save(account);
        return account;
    }

    public void deleteAccount(Account account) {
        if(accountRepository.existsAccountByUsername(account.getUsername()) == false){
            throw new RuntimeException(CANNOT_FIND_USER);
        }
        accountRepository.delete(account);
    }
}

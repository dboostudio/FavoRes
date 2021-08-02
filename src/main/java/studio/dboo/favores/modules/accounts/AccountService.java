package studio.dboo.favores.modules.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    /** Bean */
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /** Constant */
    private static final String CANNOT_FIND_USER = "유저명이나 이메일로 가입된 계정이 없습니다.";

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(usernameOrEmail);
        if (account == null) {
            account = accountRepository.findByUsername(usernameOrEmail);
        }

        if (account == null) {
            throw new UsernameNotFoundException(usernameOrEmail);
        }

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account createAccount(Account account){
        if(accountRepository.existsAccountByUsernameOrEmail(account.getUsername()) == false){
            throw new RuntimeException(CANNOT_FIND_USER);
        }
        account.encodePassword(passwordEncoder);
        return account;
    }

    public Account getAccount(Account account){
        if(accountRepository.existsAccountByUsernameOrEmail(account.getUsername()) == false){
            throw new RuntimeException(CANNOT_FIND_USER);
        }
        return account;
    }
}

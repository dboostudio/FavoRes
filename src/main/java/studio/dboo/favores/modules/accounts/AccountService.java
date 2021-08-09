package studio.dboo.favores.modules.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import studio.dboo.favores.modules.accounts.entity.Account;
import studio.dboo.favores.modules.accounts.object.UserAccount;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    /** Bean */
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /** Constant */
    private static final String CANNOT_FIND_USER = "해당 유저명으로 가입된 계정이 없습니다.";
    private static final String ALREADY_EXIST_USER = "이미 해당 유저명으로 가입된 계정이 있습니다.";
    private static final String PASSWORD_NOT_MATCH = "비밀번호가 일치하지 않습니다.";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.getAccount(username);

        if(account == null){
            throw new UsernameNotFoundException(username);
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

    public Account getAccount(String username){
        Account account = accountRepository.findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
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

    public void login(Account account) {
        Account byUsername = accountRepository.findByUsername(account.getUsername());
        if(byUsername == null){
            throw new UsernameNotFoundException(account.getUsername());
        } else if(passwordEncoder.matches(account.getPassword(),byUsername.getPassword())){
            throw new RuntimeException(PASSWORD_NOT_MATCH);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

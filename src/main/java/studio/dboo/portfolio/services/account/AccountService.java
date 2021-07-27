package studio.dboo.portfolio.services.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccountByNickname(String nickname){
        Account account = accountRepository.findByNickname(nickname);
        return account;
    }
}

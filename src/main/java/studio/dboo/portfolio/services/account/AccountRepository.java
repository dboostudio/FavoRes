package studio.dboo.portfolio.services.account;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Account findByEmail(String email);
    Account findByNickname(String nickname);

}

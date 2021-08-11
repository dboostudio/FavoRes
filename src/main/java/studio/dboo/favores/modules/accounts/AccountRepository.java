
package studio.dboo.favores.modules.accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import studio.dboo.favores.modules.accounts.entity.Account;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
    Account findByEmail(String email);
    boolean existsAccountByUsername(String username);
    boolean existsAccountByEmail(String email);
}

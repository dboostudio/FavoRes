
package studio.dboo.favores.modules.accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String emailOrNickname);
    Account findByEmail(String emailOrNickname);

}

package studio.dboo.favores.modules.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import studio.dboo.favores.modules.accounts.AccountService;
import studio.dboo.favores.modules.accounts.entity.Account;

@Component
public class GroupGenerator implements ApplicationRunner {

    @Autowired GroupsRepository groupsRepository;
    @Autowired GroupsService groupsService;
    @Autowired AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 5개의 그룹을 만들고,


        // 10명의 회원을 만들어서 2명씩 그룹에 넣어두자.
        accountService.createAccount(Account.builder()
                .username("dboo")
                .password("1234")
                .build());
    }
}

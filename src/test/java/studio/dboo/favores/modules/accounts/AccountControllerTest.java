package studio.dboo.favores.modules.accounts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;

    @DisplayName("로그인_성공")
    @Test
    @Transactional
    public void login_success() throws Exception {
        String username = "dbooa";
        String password = "123";
        String email = "dboo.studio@gmail.com";
        Account user = this.createUser(username, password, email);

        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andExpect(authenticated());
    }

    @DisplayName("로그인_실패")
    @Test
    @Transactional
    public void login_fail() throws Exception {
        String username = "dboo";
        String password = "123";
        String email = "dboo.studio@gmail.com";
        this.createUser(username, password, email);

        mockMvc.perform(formLogin().user(username).password(password+"0"))
                .andExpect(unauthenticated());
    }

    private Account createUser(String username, String password, String email) {
        Account account = Account.builder()
                .username(username)
                .password(password)
                .email(email)
                .role("USER")
                .build();
        accountService.createAccount(account);
        return account;
    }

}
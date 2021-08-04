package studio.dboo.favores.modules.accounts;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import studio.dboo.favores.modules.accounts.entity.Account;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;

    @BeforeEach
    @Transactional
    public void createTestAccounts(){
        for(int i = 0; i<50; i++){
            Account newAccount = this.createUser("test" + i, "1234","test" + i + "@test.com");
            if(i == 0){
                newAccount.setAge(31);
                newAccount.setTier(5);
                newAccount.setPoint(1000L);
                newAccount.setAddress("제주특별자치도 제주시 독짓골2길 26 수선화아파트 마동 302호");
            }
            accountRepository.save(newAccount);
        }
    }

    @DisplayName("계정조회_성공")
    @Test
    @Transactional
    public void getAccount_success() throws Exception {
        String username = "test1";
        mockMvc.perform(get("/api/account").param("username", username))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("계정생성_성공")
    @Test
    @Transactional
    public void create_success() throws Exception {

    }

    @DisplayName("로그인_성공")
    @Test
    @Transactional
    public void login_success() throws Exception {
        String username = "dbooa";
        String password = "123";
        String email = "dboo.studio@gmail.com";
        Account user = this.createUser(username, password, email);

        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andDo(print())
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
                .andDo(print())
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
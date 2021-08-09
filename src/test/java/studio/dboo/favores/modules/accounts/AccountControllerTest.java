package studio.dboo.favores.modules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import studio.dboo.favores.modules.accounts.entity.Account;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    public void createTestAccounts(){
        for(int i = 0; i<50; i++){
            createUser(
                    "test"+i,
                    "1234",
                    "test"+i+"@test.com"
            );
        }
    }

    @DisplayName("테스트환경 테스트")
    @Test
    public void testEnvironment(){
        System.out.println("성공!");
    }

    @DisplayName("계정조회_성공")
    @Test
    public void getAccount_success() throws Exception {
        // TODO - 로그인한 계정이 본인 계정에 한해서만 조회할수 있도록 변경
        String username = "test1";
        mockMvc.perform(get("/api/account").param("username", username))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("계정조회_실패")
    @Test
    public void getAccount_fail() throws Exception {
        String username = "@!#$%%%";
        mockMvc.perform(get("/api/account").param("username", username))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @DisplayName("계정생성_성공")
    @Test
    public void createAccount_success() throws Exception {
        Account account = Account.builder()
                .username("favores")
                .password("1234")
                .email("favores@gmail.com")
                .build();
        mockMvc.perform(post("/api/account")
                    .content("{\"username\":\"favores\"" +
                            ",\"email\":\"favores@gmail.com\"" +
                            ",\"password\":\"1234\",\"groups\":[]}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
        Account byUsername = accountRepository.findByUsername(account.getUsername());
    }

    @DisplayName("계정생성_실패(중복이름)")
    @Test
    public void createAccount_fail() throws Exception {
        Account account  = Account.builder()
                .username("test1")
                .password("1234")
                .email("favores@gmail.com")
                .build();
        String param = objectMapper.writeValueAsString(account);
        mockMvc.perform(post("/api/account")
                    .content(param)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("폼로그인_성공")
    @Test
    public void formLogin_success() throws Exception {
        String password = "1234";
        Account account = Account.builder()
                .username("favores")
                .password(password)
                .email("favores@gmail.com")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(formLogin().user(account.getUsername()).password(password))
                .andDo(print())
                .andExpect(authenticated());
    }

    @DisplayName("폼로그인_실패_사용자미존재")
    @Test
    public void formLogin_fail_user_not_found() throws Exception {
        String password = "1234";
        Account account = Account.builder()
                .username("favores")
                .password(password)
                .email("favores@gmail.com")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(formLogin().user(account.getUsername() + "___").password(password))
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @DisplayName("폼로그인_실패_패스워드불일치")
    @Test
    public void formLogin_fail_wrong_password() throws Exception {
        String password = "1234";
        Account account = Account.builder()
                .username("favores")
                .password(password)
                .email("favores@gmail.com")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(formLogin().user(account.getUsername()).password(password + "1"))
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @DisplayName("로그인_성공")
    @Test
    public void login_success() throws Exception {
        Account account = Account.builder()
                .username("dboo")
                .password("1234")
                .email("dboo.studio@gmail.com")
                .role("USER")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(post("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"dboo\",\"password\":\"1234\"}")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("로그인_실패_사용자미존재")
    @Test
    public void login_fail_user_not_found() throws Exception {
        Account account = Account.builder()
                .username("dboo")
                .password("1234")
                .email("dboo.studio@gmail.com")
                .role("USER")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(post("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test1\",\"password\":\"1234\"}")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());;
    }

    @DisplayName("로그인_실패_패스워드불일치")
    @Test
    public void login_fail_wrong_password() throws Exception {
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
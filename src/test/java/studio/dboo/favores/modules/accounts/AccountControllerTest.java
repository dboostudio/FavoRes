package studio.dboo.favores.modules.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import studio.dboo.favores.modules.accounts.entity.Account;

import javax.transaction.Transactional;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
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

    private ArrayList<String> jwtTokenList = new ArrayList<>();

    @BeforeEach
    public void createTestAccountsAndGetTokens(){
        for(int i = 0; i<10; i++){
            Account createdAccount = this.createUser(
                    "test"+i,
                    "1234",
                    "test"+i+"@test.com"
            );
            Account loginAccount = Account.builder().username("test"+i).password("1234").build();
            jwtTokenList.add(accountService.loginAndGenerateToken(loginAccount));
        }
    }

    @DisplayName("??????????????? ?????????")
    @Test
    public void testEnvironment(){
        System.out.println("??????!");
    }

    @DisplayName("????????????_??????")
    @Test
    public void getAccount_success() throws Exception {
        // TODO - ???????????? ????????? ?????? ????????? ???????????? ???????????? ????????? ??????
        String username = "test1";
        mockMvc.perform(get("/api/account").param("username", username)
                    .header("Authorization", "Bearer " + jwtTokenList.get(1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("????????????_??????")
    @Test
    public void getAccount_fail() throws Exception {
        String username = "@!#$%%%";
        mockMvc.perform(get("/api/account").param("username", username))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @DisplayName("????????????_??????")
    @Test
    public void createAccount_success() throws Exception {
        mockMvc.perform(post("/api/account")
                    .content("{\"username\":\"test\"" +
                            ",\"email\":\"test@gmail.com\"" +
                            ",\"password\":\"1234\",\"groups\":[]}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("????????????_??????(????????????)")
    @Test
    public void createAccount_fail() throws Exception {
        mockMvc.perform(post("/api/account")
                .content("{\"username\":\"test\"" +
                        ",\"email\":\"test@gmail.com\"" +
                        ",\"password\":\"1234\",\"groups\":[]}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("????????????_??????")
    @Test
    public void formLogin_success() throws Exception {
        String password = "1234";
        Account account = Account.builder()
                .username("test")
                .password(password)
                .email("favores@gmail.com")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(formLogin().user(account.getUsername()).password(password))
                .andDo(print())
                .andExpect(authenticated());
    }

    @DisplayName("????????????_??????_??????????????????")
    @Test
    public void formLogin_fail_user_not_found() throws Exception {
        String password = "1234";
        Account account = Account.builder()
                .username("test")
                .password(password)
                .email("favores@gmail.com")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(formLogin().user(account.getUsername() + "___").password(password))
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @DisplayName("????????????_??????_?????????????????????")
    @Test
    public void formLogin_fail_wrong_password() throws Exception {
        String password = "1234";
        Account account = Account.builder()
                .username("test")
                .password(password)
                .email("favores@gmail.com")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(formLogin().user(account.getUsername()).password(password + "1"))
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @DisplayName("JWT ??????????????????_??????")
    @Test
    public void login_success() throws Exception {
        Account account = Account.builder()
                .username("test")
                .password("1234")
                .email("test@gmail.com")
                .role("USER")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(post("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test\",\"password\":\"1234\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("?????????_??????_??????????????????")
    @Test
    public void login_fail_user_not_found() throws Exception {
        Account account = Account.builder()
                .username("test")
                .password("1234")
                .email("test@gmail.com")
                .role("USER")
                .build();

        accountService.createAccount(account);

        mockMvc.perform(post("/api/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test1\",\"password\":\"1234\"}"))
                .andDo(print())
                .andExpect(status().isOk());;
    }

    @DisplayName("?????????_??????_?????????????????????")
    @Test
    public void login_fail_wrong_password() throws Exception {
        String username = "test";
        String password = "123";
        String email = "test@gmail.com";
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
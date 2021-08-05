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
        String param = objectMapper.writeValueAsString(account);
        System.out.println(param);
        mockMvc.perform(post("/api/account").content("{\"username\":\"favores\",\"email\":\"favores@gmail.com\",\"password\":\"1234\",\"groups\":[]}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
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
        mockMvc.perform(post("/api/account").content(param).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @DisplayName("로그인_성공")
    @Test
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
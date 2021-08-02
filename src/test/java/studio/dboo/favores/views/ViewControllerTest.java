package studio.dboo.favores.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import studio.dboo.favores.modules.accounts.AccountService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ViewControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
}
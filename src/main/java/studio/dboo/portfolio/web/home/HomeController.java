package studio.dboo.portfolio.web.home;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.dboo.portfolio.services.account.security.SecurityMessage;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "홈페이지";
    }
}

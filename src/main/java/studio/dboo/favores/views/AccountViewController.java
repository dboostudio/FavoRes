package studio.dboo.favores.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AccountViewController {

    @GetMapping("/sign-up")
    public String signUp(Model model){
        return "account/sign-up";
    }
}

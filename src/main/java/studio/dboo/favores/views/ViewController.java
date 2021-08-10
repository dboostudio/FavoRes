package studio.dboo.favores.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(Model model, Principal principal){
        transferUsernameToModel(model, principal);
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal){
        transferUsernameToModel(model, principal);
        return "admin";
    }

    @GetMapping("/groups")
    public String groups(Model model, Principal principal){
        transferUsernameToModel(model, principal);
        return "groups";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model){
        return "account/sign-up";
    }

    private void transferUsernameToModel(Model model, Principal principal) {
        if(principal == null){
            model.addAttribute("username", "Welcome To FavoRes");
        } else {
            model.addAttribute("username", principal.getName());
        }
    }


}

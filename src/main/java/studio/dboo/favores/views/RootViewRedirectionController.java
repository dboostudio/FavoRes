package studio.dboo.favores.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootViewRedirectionController {

    @RequestMapping("/")
    public String root() {
        return "index";
    }
}

package studio.dboo.favores.modules.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    @GetMapping()
    public void getAccount(){
        //TODO - Current User 정보 받아오기
    }

    @PostMapping
    public void createAccount(){
        System.out.println("test");
    }


}

package studio.dboo.favores.modules.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.dboo.favores.modules.accounts.entity.Account;

import javax.validation.Valid;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Account> getAccount(@Valid @RequestBody Account account){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(account));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.createAccount(account));
    }

    @PutMapping
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody Account account){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(account));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(@Valid @RequestBody Account account){
        accountService.deleteAccount(account);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

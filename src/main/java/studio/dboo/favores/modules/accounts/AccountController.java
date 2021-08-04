package studio.dboo.favores.modules.accounts;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.dboo.favores.modules.accounts.entity.Account;
import studio.dboo.favores.modules.annotation.RestControllerLogger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
@Api(tags = {"계정 CRUD"})
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @RestControllerLogger
    @GetMapping
    @ApiOperation(value = "getAccount", notes = "계정 조회")
    public ResponseEntity<Account> getAccount(@Valid @RequestBody String username){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccount(username));
    }

    @PostMapping
    @ApiOperation(value = "createAccount", notes = "계정 생성")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account, HttpServletRequest request) throws URISyntaxException {
        Account savedAccount = accountService.createAccount(account);
        return ResponseEntity.created(new URI(request.getRequestURI() + "/" + savedAccount.getId())).body(savedAccount);
    }

    @PutMapping
    @ApiOperation(value = "updateAccount", notes = "계정 정보 갱신")
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody Account account){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(account));
    }

    @DeleteMapping
    @ApiOperation(value = "deleteAccount", notes = "계정 삭제")
    public ResponseEntity<String> deleteAccount(@Valid @RequestBody Account account){
        accountService.deleteAccount(account);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

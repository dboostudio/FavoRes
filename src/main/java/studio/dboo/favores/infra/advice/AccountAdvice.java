package studio.dboo.favores.infra.advice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(value = "studio.dboo.favores.modules.accounts")
public class AccountAdvice {

    // 유저이름을 찾지 못한 경우
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity usernameNotFoundExceptionHandler(AuthenticationException e){
        JsonArray result = new JsonArray();
        JsonObject jsonObject = new JsonObject();

        log.error("========== FAVORES ERROR LOG START ==========");
        log.error("AuthenticationException occured");
        log.error("Error SimpleName : {} \n Error Message : {} \n Error StackTrace : {} ", e.getClass().getSimpleName(), e.getMessage(), e);
        log.error("========== FAVORES ERROR LOG END ============");

        jsonObject.addProperty("field", e.getClass().getSimpleName());
        jsonObject.addProperty("message", e.getMessage());
        result.add(jsonObject);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.toString());
    }
}

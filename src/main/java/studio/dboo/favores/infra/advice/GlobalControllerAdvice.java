package studio.dboo.favores.infra.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    // 핸들러가 설정되지 않은 오류일 시, INTERNAL_SERVER_ERROR 리턴
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity globalExceptionHandler(Exception e){
        JsonArray result = new JsonArray();
        JsonObject jsonObject = new JsonObject();

        log.error("========== FAVORES ERROR LOG START ==========");
        log.error("Error SimpleName : {} \n Error Message : {} \n Error StackTrace : {} ", e.getClass().getSimpleName(), e.getMessage(), e);
        log.error("========== FAVORES ERROR LOG END ============");

        jsonObject.addProperty("field", e.getClass().getSimpleName());
        jsonObject.addProperty("message", e.getMessage());
        result.add(jsonObject);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.toString());
    }

    // Validation 실패 시, BAD_REQUEST 리턴
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgNotValidExceptionHandler(MethodArgumentNotValidException e) throws JsonProcessingException {
        JsonArray result = new JsonArray();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach( error -> {
            JsonObject jsonObject = new JsonObject();
            FieldError field = (FieldError) error;
            log.error("========== FAVORES ERROR LOG START ==========");
            log.error("MethodArgumentNotValidException occured");
            log.error("\n- Field : {}\n- ObjectName : {}\n- DefaultMessage : {}\n- RejectedValue : {}",
                    field.getField(), field.getObjectName(), field.getDefaultMessage(), field.getRejectedValue());
            jsonObject.addProperty("field", field.getField());
            jsonObject.addProperty("message", field.getDefaultMessage());
            result.add(jsonObject);
            log.error("========== FAVORES ERROR LOG END ============");
        });
        return ResponseEntity.badRequest().body(result.toString());
    }
}

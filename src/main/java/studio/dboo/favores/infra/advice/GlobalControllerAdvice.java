package studio.dboo.favores.infra.advice;

import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity exception(Exception e){

        log.error("========== FAVORES ERROR LOG START ==========");
        log.error("Error SimpleName : {} \n Error Message : {]", e.getClass().getSimpleName(), e.getMessage());
        log.error("========== FAVORES ERROR LOG END ============");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Exception Occurred\n" +
                        "에러 이름 : " + e.getClass().getSimpleName() + "\n" +
                        "에러 내용 : " + e.getMessage());
    }

    // Validation 실패 시, BAD_REQUEST 리턴
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach( error -> {
            FieldError field = (FieldError) error;
            log.error("========== FAVORES ERROR LOG START ==========");
            log.error("MethodArgumentNotValidException occured");
            log.error("\n- Field : {}\n- ObjectName : {}\n- DefaultMessage : {}\n- RejectedValue : {}",
                    field.getField(), field.getObjectName(), field.getDefaultMessage(), field.getRejectedValue());

            log.error("========== FAVORES ERROR LOG END ============");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

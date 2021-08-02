package studio.dboo.favores.infra.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Exception Occurred\n" +
                        "에러 이름 : " + e.getClass().getSimpleName() + "\n" +
                        "에러 내용 : " + e.getMessage());
    }

    // Validation 실패시 BAD_REQUEST 리턴
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach( error -> {
            FieldError field = (FieldError) error;
            String fieldName = field.getObjectName();
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString();
//            System.out.println(fieldName);
//            System.out.println(message);
//            System.out.println(value);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

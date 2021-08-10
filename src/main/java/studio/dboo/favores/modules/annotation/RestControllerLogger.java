package studio.dboo.favores.modules.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestControllerLogger {
    // 해당 어노테이션이 붙어 있을 시, ResControllerAOP에서 로그를 남긴다.
}

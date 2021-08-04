package studio.dboo.favores.infra.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RestControllerAOP {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Pointcut("execution(* studio.dboo.favores.modules..*.*(..))") //AOP기능 적용지점 설정
//    private void cut(){}

    @Pointcut("@annotation(studio.dboo.favores.modules.annotation.RestControllerLogger)")
    public void restControllerAOP(){}

    @Around("restControllerAOP()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("========= " + joinPoint.getTarget().getClass().getSimpleName()
                + " >> " + method.getName() + " ==========");

        Object[] args = joinPoint.getArgs();
        if(args.length == 0) {
            log.info("호출 파라미터가 없습니다.");
        } else {
            log.info("호출 파라미터는 다음과 같습니다.");
        }
        for(Object obj : args){
            log.info("PARAMETER NAME : " + obj.getClass().getSimpleName()
                    + "VALUE : " + obj);
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed(); //메소드 실행

        stopWatch.stop();

        log.info("해당 메소드는 총 " + stopWatch.getTotalTimeSeconds() + "초 걸렸습니다.");
        log.info("결과값 : " + result);
        log.info("========== END ==========");
    }
}

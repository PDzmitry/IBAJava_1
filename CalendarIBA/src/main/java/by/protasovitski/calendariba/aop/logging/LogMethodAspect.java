package by.protasovitski.calendariba.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LogMethodAspect {
    Authentication auth;

//    @Pointcut("@annotation(by.protasovitski.calendariba.aop.logging.LogMethod)")
    @Pointcut("within(by.protasovitski.calendariba.service.impl.*)")
    public void callAtMethod() {

    }

    @Before("callAtMethod()")
    public void beforeCallMethod(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs()).
                map(a -> a.toString()).collect(Collectors.joining(","));
        auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("User: {} is calling the method " + jp.toShortString() + ", args=[" + args + "]",auth.getName());

    }

   /* @Around("callAtMethod()")
    public Object aroundAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Arguments passed to method are: " + Arrays.toString(joinPoint.getArgs()));
        final Object result = joinPoint.proceed();
        log.info("Result from method is: " + result);
        return result;
    }*/

    @After("callAtMethod()")
//    @AfterReturning("callAtMethod()")
    public void afterCallAt(JoinPoint jp) {
        log.info("User: {} done calling the method " + jp.toShortString(),auth.getName());
    }
}

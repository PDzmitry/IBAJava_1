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
public class OwnerVerificationAspect {
    @Pointcut("@annotation(by.protasovitski.calendariba.aop.logging.OwnerVerification)")
    public void callAtMethod(){

    }
    @Before("callAtMethod()")
    public void beforeCallMethod(JoinPoint jp){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
  /*      String args = Arrays.stream(jp.getArgs()).
                map(a->a.toString()).collect(Collectors.joining(","));*/
//        log.info("before" + jp.toString()+", args=[" + args + "]");
        String args =Arrays.stream(jp.getArgs())
                .map(a-> a.getClass().getName()).collect(Collectors.joining(","));
        log.info("before " + jp.toString()+", args=[" + args + "]");
        log.info("Работает пользователь:"+username);
        

    }

    @After("callAtMethod()")
    public void afterCallAt(JoinPoint jp){
        log.info("after" + jp.toString());
    }
}

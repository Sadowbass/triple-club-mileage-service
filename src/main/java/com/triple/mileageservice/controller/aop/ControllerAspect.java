package com.triple.mileageservice.controller.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ControllerAspect {

    private final ObjectMapper om;

    @Around(value = "execution(* com.triple.mileageservice.controller.EventController.events(..))")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.info("request data : {}", om.writeValueAsString(args[0]));

        Object proceed = joinPoint.proceed();

        log.info("response data : {}", om.writeValueAsString(proceed));
        return proceed;
    }
}

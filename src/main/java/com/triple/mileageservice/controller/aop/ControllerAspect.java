package com.triple.mileageservice.controller.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.mileageservice.controller.dtos.EventResponse;
import com.triple.mileageservice.domain.mileage.dto.MileageDetailResponse;
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
    public Object aroundEventController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.info("request data : {}", om.writeValueAsString(args[0]));

        Object proceed = joinPoint.proceed();

        log.info("response data : {}", om.writeValueAsString(proceed));
        return proceed;
    }

    @Around(value = "execution(* com.triple.mileageservice.controller.MileageController.*(..))")
    public Object aroundMileageController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length == 1) {
            log.info("request userId : {}", args[0]);
        } else {
            log.info("request userId : {}, pageNum : {}, order : {}", args[0], args[1], args[2]);
        }

        Object proceed = joinPoint.proceed();

        EventResponse response = (EventResponse) proceed;
        if (response.getResult() instanceof MileageDetailResponse) {
            MileageDetailResponse cast = (MileageDetailResponse) response.getResult();
            log.info("response {} data. userId : {}", cast.getTotalCount(), args[0]);
        } else {
            log.info("response success. userId : {}", args[0]);
        }

        return proceed;
    }
}

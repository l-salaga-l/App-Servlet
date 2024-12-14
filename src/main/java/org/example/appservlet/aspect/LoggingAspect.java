package org.example.appservlet.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("execution(public * org.example.appservlet.controller.*.*(..))")
    public void controllerPointcut() {}

    @SneakyThrows
    @Before(value = "controllerPointcut()")
    public void loggingControllers(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        String httpMethod = request.getMethod();
        log.info("Вызов эндпоинта по пути {} {}, контроллер: {}, метод: {}", path, httpMethod, className, methodName);
    }
}

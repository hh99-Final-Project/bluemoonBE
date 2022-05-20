package com.sparta.bluemoon.util;

import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.user.User;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import javax.print.DocFlavor.STRING;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@Profile("dev")
public class TimeTraceAop {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.sparta.bluemoon..*(..))" +
        "&& !execution(* com.sparta.bluemoon.security..*(..)) ")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        logger.info("START: " + joinPoint.toString());

        Object[] parameterValues = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        if (signature.getParameterNames() != null) {
            String[] parameterNames = signature.getParameterNames();
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                if (parameterName.equals("userDetails")) {
                    UserDetailsImpl userDetails = (UserDetailsImpl) parameterValues[i];
                    User user = userDetails.getUser();
                    String[] classNames = joinPoint.getTarget().getClass().getName().split("[.]");
                    String className = classNames[classNames.length - 1];
                    String methodName = signature.getMethod().getName();
                    logger.info(String.format("CustomInfo: %s (%s)님이 %s 클래스 %s 메서드를 실행하였습니다. [%s]", user.getUsername(), user.getId(), className, methodName,
                        LocalDateTime.now()));
                }
            }
        }

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            logger.info("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}

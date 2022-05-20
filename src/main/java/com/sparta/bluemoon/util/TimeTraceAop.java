package com.sparta.bluemoon.util;

import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.user.User;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
public class TimeTraceAop {

    @Around("execution(* sparta.bluemoon..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        

        Object[] parameterValues = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        for (int i = 0; i < method.getParameters().length; i++) {
            String parameterName = method.getParameters()[i].getName();

            if (parameterName.equals("userDetails")) {
                UserDetailsImpl userDetails = (UserDetailsImpl) parameterValues[i];
                User user = userDetails.getUser();
                System.out.println("user.getUsername() = " + user.getUsername());
            }
        }

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}

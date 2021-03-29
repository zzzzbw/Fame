package com.zbw.fame.config;

import com.zbw.fame.util.FameUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author by zzzzbw
 * @since 2021/02/18 14:18
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAop {
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controller() {
    }

    @Around("controller() || restController()")
    public Object controller(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        if (method == null) {
            return joinPoint.proceed();
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        HttpServletRequest request = FameUtils.getRequest();

        printRequestLog(request, className, methodName, args);
        final Object returnObj = joinPoint.proceed();
        printResponseLog(className, methodName, returnObj);

        return returnObj;
    }

    private void printRequestLog(HttpServletRequest request, String clazzName, String methodName,
                                 Object[] args) {
        log.info("Request URL: [{}], URI: [{}], Request Method: [{}], IP: [{}]",
                request.getRequestURL(),
                request.getRequestURI(),
                request.getMethod(),
                FameUtils.getIp());

        boolean shouldNotLog = false;
        for (Object arg : args) {
            if (arg == null
                    || arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse
                    || arg instanceof MultipartFile
                    || arg.getClass().isAssignableFrom(MultipartFile[].class)) {
                shouldNotLog = true;
                break;
            }
        }

        if (!shouldNotLog) {
            String requestBody = FameUtils.objectToJson(args);
            log.debug("{}.{} Parameters: [{}]", clazzName, methodName, requestBody);
        }
    }

    private void printResponseLog(String className, String methodName,
                                  Object returnObj) {
        String returnData = "";
        if (returnObj != null) {
            if (returnObj instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnObj;
                if (responseEntity.getBody() instanceof Resource) {
                    returnData = "[ BINARY DATA ]";
                } else if (responseEntity.getBody() != null) {
                    returnData = toString(responseEntity.getBody());
                }
            } else {
                returnData = toString(returnObj);
            }


            log.debug("{}.{} Response: [{}]", className, methodName, returnData);
        }
    }

    @NonNull
    private String toString(@NonNull Object obj) {
        Assert.notNull(obj, "Return object must not be null");

        String toString;
        if (obj.getClass().isAssignableFrom(byte[].class) && obj instanceof Resource) {
            toString = "[ BINARY DATA ]";
        } else {
            toString = FameUtils.objectToJson(obj);
        }
        return toString;
    }
}

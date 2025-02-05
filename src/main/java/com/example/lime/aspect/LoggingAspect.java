package com.example.lime.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'").withZone(ZoneId.of("UTC"));

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Around("restControllerMethods()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString().substring(0, 6);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        Map<String, String[]> queryParams = request.getParameterMap();
        String queryString = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));

        Object requestBody = null;
        Object[] args = joinPoint.getArgs();
        Class<?>[] parameterTypes = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
        }

        try {
            Method methodSignature = joinPoint.getSignature().getDeclaringType().getDeclaredMethod(joinPoint.getSignature().getName(), parameterTypes);
            Parameter[] parameters = methodSignature.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                    requestBody = args[i];
                    break;
                }
            }
        } catch (NoSuchMethodException e) {
            logger.error("Method not found: {}", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        }

        logger.info("Incoming Request: Method={} URL={} Timestamp={} RequestId={}", method, url + (queryString.isEmpty() ? "" : "?" + queryString), formatter.format(Instant.now()), requestId);
        if (requestBody != null) {
            logger.info("Request Body: {}", requestBody);
        }

        Object response = null;
        try {
            response = joinPoint.proceed();
        } catch (HttpMessageNotReadableException ex) {
            logger.error("Error while processing request: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception occurred in method: {} with message: {}", joinPoint.getSignature().getName(), ex.getMessage());
            throw ex;
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        String loggableResponse = url.contains("/auth") ? "Response from /auth blurred" : (response != null ? response.toString() : "null");

        logger.info("Response: {} Execution Time={}ms RequestId={}", loggableResponse, executionTime, requestId);
        return response;
    }
}
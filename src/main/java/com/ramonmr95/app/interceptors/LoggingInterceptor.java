package com.ramonmr95.app.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Interceptor
public class LoggingInterceptor {

	@AroundInvoke
	public Object intercept(InvocationContext context) throws Exception {
        Logger log = LogManager.getLogger(context.getClass());
        String className = context.getTarget().getClass().getSimpleName();
        log.info("Entering: " + className + ", Method: " + context.getMethod().getName());
        Object result = context.proceed();
        log.info("Exiting: " + className + ", Method: "  + context.getMethod().getName());
		return result;
	}
}

package com.naturalprogrammer.spring5tutorial.web.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RequestMonitor {
	
	private static Log log = LogFactory.getLog(RequestMonitor.class);

	@Around("within(com.naturalprogrammer.spring5tutorial.web.controllers.*)")
	public Object wrap(ProceedingJoinPoint pjp) throws Throwable {
		log.debug("Before handler " + pjp.getSignature().getName()
				+ ". Thread " + Thread.currentThread().getName());
		
		Object retval = pjp.proceed();
		
		log.debug("Handler " + pjp.getSignature().getName()
				+ " execution successful.");
		
		return retval;
	}
}

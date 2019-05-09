package com.naturalprogrammer.spring5tutorial.web.aop;

import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class NeedsConfirmPasswordAspect {

    private static Log log = LogFactory.getLog(NeedsConfirmPasswordAspect.class);

    @Before("@annotation(com.naturalprogrammer.spring5tutorial.web.aop.NeedsConfirmPassword)")
    public void confirmPassword(JoinPoint pjp) throws Throwable {

        User u = MyUtils.currentUser().orElse(null);
        if (u!= null){
            System.out.println("Redirecting to confirm password");
//            MyUtils.logout();
        }
    }
}

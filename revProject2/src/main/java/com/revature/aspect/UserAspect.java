package com.revature.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class UserAspect {

	@Pointcut("execution(* com.revature.controller.LoginController.login(..))")
	public void loginByIdMethodInLoginController() {}

}

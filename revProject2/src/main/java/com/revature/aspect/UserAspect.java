package com.revature.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revature.dto.MessageDTO;

@Aspect
@Component
public class UserAspect {

	@Pointcut("execution(* com.revature.controller.LoginController.login(..))")
	public void loginByIdMethodInLoginController() {}
/*	
	// Utilizing pointcut template
	@Before("getUserByIdMethodInUserController()")
	public void adviceBeforeGetUserbyId(JoinPoint jp) {
			
		String method = jp.getSignature().toLongString();
			
		System.out.println("Executing @Before advice on method: " + method);
		// You could do logging, etc in here
			
	}
		
	@AfterReturning(
			pointcut = 
			"execution(public * com.revature.controller.UserController.getUserById(..))",
			returning = "result")
	public void afterReturningGetUserById(JoinPoint jp, Object result) {
		
		Object[] args = jp.getArgs();
		for (Object o : args) {
			System.out.println(o);
		}
		
		if (result instanceof ResponseEntity) {
			Object obj = ((ResponseEntity) result).getBody();
			
			System.out.println(obj);
			
			if (obj instanceof MessageDTO) {
				System.out.println("MessageTemplate's message: " + ((MessageDTO) obj).getMessage());
			}
		}
	}
*/
}

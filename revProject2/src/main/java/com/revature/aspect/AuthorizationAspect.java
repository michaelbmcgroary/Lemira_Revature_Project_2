package com.revature.aspect;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revature.dto.MessageDTO;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;

@Aspect
@Component
public class AuthorizationAspect {

	@Autowired
	private HttpServletRequest request;
	
	@Around("@annotation(com.revature.annotations.LoggedInOnly)")
	public ResponseEntity<Object> protectEndpointForLoggedInUsersOnly(ProceedingJoinPoint pjp) throws Throwable {
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("currentlyLoggedInUser") == null) {
			return ResponseEntity.status(401).body(new MessageDTO("You must be logged in to access this resource"));
		}
		@SuppressWarnings("unchecked")
		ResponseEntity<Object> result = (ResponseEntity<Object>) pjp.proceed(pjp.getArgs());
		return result;
	}
	
	@Around("@annotation(com.revature.annotations.ModeratorOnly)")
	public ResponseEntity<Object> protectEndpointForModeratorsOnly(ProceedingJoinPoint pjp) throws Throwable {
		HttpSession session = request.getSession(false);
		if(session == null || ((User) session.getAttribute("currentlyLoggedInUser")).getUserType().equals(new UserType(1))) {
			return ResponseEntity.status(401).body(new MessageDTO("You must be a Moderator to access this resource"));
		}
		@SuppressWarnings("unchecked")
		ResponseEntity<Object> result = (ResponseEntity<Object>) pjp.proceed(pjp.getArgs());
		return result;
	}
	
	@Around("@annotation(com.revature.annotations.UnbannedOnly)")
	public ResponseEntity<Object> protectEndpointForActiveUsersOnly(ProceedingJoinPoint pjp) throws Throwable {
		HttpSession session = request.getSession(false);
		if(session == null || ((User) session.getAttribute("currentlyLoggedInUser")).getUserStatus().equals(new UserStatus(2))) {
			return ResponseEntity.status(401).body(new MessageDTO("You must be an unbanned user to access this resource"));
		}
		@SuppressWarnings("unchecked")
		ResponseEntity<Object> result = (ResponseEntity<Object>) pjp.proceed(pjp.getArgs());
		return result;
	}

	
}


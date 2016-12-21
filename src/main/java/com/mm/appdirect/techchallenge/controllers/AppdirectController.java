package com.mm.appdirect.techchallenge.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth.provider.ConsumerAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mm.appdirect.techchallenge.api.EventResult;
import com.mm.appdirect.techchallenge.api.event.ErrorCodes;
import com.mm.appdirect.techchallenge.api.event.SubscriptionCancelEvent;
import com.mm.appdirect.techchallenge.api.event.SubscriptionEvent;
import com.mm.appdirect.techchallenge.api.event.UserAssignEvent;
import com.mm.appdirect.techchallenge.api.event.UserUnassignEvent;
import com.mm.appdirect.techchallenge.exception.ApplicationException;
import com.mm.appdirect.techchallenge.service.AccountService;
import com.mm.appdirect.techchallenge.service.AppDirectService;
import com.mm.appdirect.techchallenge.service.UserService;

@RestController
@RequestMapping("/appdirect")
public class AppdirectController {
	@Autowired 
    private UserService userSvc;
	@Autowired 
    private AccountService accountSvc;
	@Autowired
	private AppDirectService appDirectSvc;
	
	Logger logger = LoggerFactory.getLogger(AppdirectController.class);
	
	@RequestMapping(path="/subscription/create")
    public EventResult createSubscription(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) {
		logger.debug("Subscription Create with {1}",url);
		SubscriptionEvent event = null;
		EventResult result = null;
		try {
			event = appDirectSvc.get(url, SubscriptionEvent.class);
		} catch (ApplicationException e) {
			result = new EventResult();
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setErrorCode(ErrorCodes.UNKNOWN_ERROR.toString());
			return result;
		}
		result = accountSvc.processNewSubscription(event);
    	return result;
    }
	
	

	@RequestMapping(path="/subscription/cancel")
    public EventResult cancelSubscription(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) {
    	
    	SubscriptionCancelEvent event = null;
		EventResult result = null;
		try {
			event = appDirectSvc.get(url, SubscriptionCancelEvent.class);
			
		} catch (ApplicationException e) {
			result = new EventResult();
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setErrorCode(ErrorCodes.UNKNOWN_ERROR.toString());
			return result;
		}
		result = accountSvc.processCancelSubscription(event);
    	return result;
    }
	
	@RequestMapping(path="/user/assign")
    public EventResult assignUser(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) {
    	
    	UserAssignEvent event = null;
		EventResult result = null;
		try {
			event = appDirectSvc.get(url, UserAssignEvent.class);
			
		} catch (ApplicationException e) {
			result = new EventResult();
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setErrorCode(ErrorCodes.UNKNOWN_ERROR.toString());
			return result;
		}
		result = userSvc.processAssignment(event);
    	return result;
    }
	
	@RequestMapping(path="/user/unassign")
    public EventResult unassignUser(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) {
    	
		UserUnassignEvent event = null;
		EventResult result = null;
		try {
			event = appDirectSvc.get(url, UserUnassignEvent.class);
			
		} catch (ApplicationException e) {
			result = new EventResult();
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setErrorCode(ErrorCodes.UNKNOWN_ERROR.toString());
			return result;
		}
		result = userSvc.processUnassignment(event);
    	return result;
    }
	
	
}

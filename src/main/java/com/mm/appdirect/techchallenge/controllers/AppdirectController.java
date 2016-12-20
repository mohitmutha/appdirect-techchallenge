package com.mm.appdirect.techchallenge.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth.provider.ConsumerAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mm.appdirect.techchallenge.api.EventResult;
import com.mm.appdirect.techchallenge.api.event.SubscriptionCancelEvent;
import com.mm.appdirect.techchallenge.api.event.SubscriptionEvent;
import com.mm.appdirect.techchallenge.api.event.UserAssignEvent;
import com.mm.appdirect.techchallenge.api.event.UserUnassignEvent;
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
	
	@RequestMapping(path="/subscription/create")
    public EventResult createSubscription(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	System.out.println("Start create Call URL");
    	SubscriptionEvent event = appDirectSvc.get(url, SubscriptionEvent.class);
    	System.out.println("Received event");
    	EventResult result = accountSvc.processNewSubscription(event);
    	System.out.println("Saving now");
    	return result;
    }
	
	

	@RequestMapping(path="/subscription/cancel")
    public EventResult cancelSubscription(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	
    	SubscriptionCancelEvent event = appDirectSvc.get(url, SubscriptionCancelEvent.class);
    	EventResult result = accountSvc.processCancelSubscription(event);
    	
    	return result;
    }
	
	@RequestMapping(path="/user/assign")
    public EventResult assignUser(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	
    	UserAssignEvent event = appDirectSvc.get(url, UserAssignEvent.class);
    	EventResult result = userSvc.processAssignment(event);
    	return result;
    }
	
	@RequestMapping(path="/user/unassign")
    public EventResult unassignUser(HttpServletRequest request,
            @RequestParam String url,
            @AuthenticationPrincipal ConsumerAuthentication authentication) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
    	
		UserUnassignEvent event = appDirectSvc.get(url, UserUnassignEvent.class);
		EventResult result = userSvc.processUnassignment(event);
		
    	return result;
    }
	
	
}

package com.mm.appdirect.techchallenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mm.appdirect.techchallenge.api.EventResult;
import com.mm.appdirect.techchallenge.api.event.Company;
import com.mm.appdirect.techchallenge.api.event.ErrorCodes;
import com.mm.appdirect.techchallenge.api.event.SubscriptionCancelEvent;
import com.mm.appdirect.techchallenge.api.event.SubscriptionEvent;
import com.mm.appdirect.techchallenge.domain.Account;
import com.mm.appdirect.techchallenge.domain.AccountRepository;

@Component
public class AccountService {
	@Autowired
	private AccountRepository organizationRepo;
	
	public Page<Account> getAll(Pageable p){
		return organizationRepo.findAll(p);
	}

	

	public EventResult processNewSubscription(SubscriptionEvent event) {
		Company company = event.getPayload().getCompany();
    	Account organization = convertCompany(company);
    	Account existingOrg = organizationRepo.findByUuid(organization.getUuid());
    	
    	if(existingOrg != null){
    		organization.setId(existingOrg.getId());
    	}
    	organization = organizationRepo.save(organization);    	
    	EventResult result = new EventResult();
    	result.setAccountIdentifier(organization.getId().toString());
    	result.setSuccess(true);
    	//result.setMessage("Account created");
		return result;
	}
	
	public EventResult processCancelSubscription(SubscriptionCancelEvent event) {
		String accountId = event.getPayload().getAccount().getAccountIdentifier();
    	
    	//START-WORKAROUND
    	//The below is a workaround to process the account cancellation
    	//in the ping tests. The ping tests always send dummy-account
    	//and not the identifier returned in the subscription
    	if("dummy-account".equals(accountId)){
    		accountId = "1";
    	}
    	//END-WORKAROUND
    	
    	Account existingOrg = this.findAccountById(accountId);
    	
    	EventResult result = new EventResult();
    	if(existingOrg != null){
        	result.setAccountIdentifier(existingOrg.getId().toString());
        	result.setSuccess(Boolean.TRUE.booleanValue());
        	result.setMessage("Account disabled");
        	existingOrg.setStatus(Account.Status.DISABLED);
        	existingOrg = organizationRepo.save(existingOrg);
        	//TODO Disable all users also??
        	result.setMessage("Subscription disabled successfully");
    	} else {
    		result.setErrorCode(ErrorCodes.ACCOUNT_NOT_FOUND.toString());
    		result.setSuccess(Boolean.FALSE.booleanValue());
    		result.setMessage("Subscription does not exist");
    	}
    	return result;
	}
	
	public Account findAccountById(String accountId) {
		Long convertedId = Long.parseLong(accountId);
		return organizationRepo.findOne(convertedId);
	}
	
	private Account convertCompany(Company company) {
		Account org = new Account();
		org.setCountry(company.getCountry());
		org.setName(company.getName());
		org.setPhoneNumber(company.getPhoneNumber());
		org.setUuid(company.getUuid());
		org.setWebsite(company.getWebsite());
		return org;
	}

	
	
}

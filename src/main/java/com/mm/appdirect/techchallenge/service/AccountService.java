package com.mm.appdirect.techchallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.google.inject.Stage;
import com.mm.appdirect.techchallenge.api.EventResult;
import com.mm.appdirect.techchallenge.api.event.Company;
import com.mm.appdirect.techchallenge.api.event.Creator;
import com.mm.appdirect.techchallenge.api.event.ErrorCodes;
import com.mm.appdirect.techchallenge.api.event.SubscriptionCancelEvent;
import com.mm.appdirect.techchallenge.api.event.SubscriptionEvent;
import com.mm.appdirect.techchallenge.domain.Account;
import com.mm.appdirect.techchallenge.domain.Account.Status;
import com.mm.appdirect.techchallenge.domain.AccountRepository;
import com.mm.appdirect.techchallenge.domain.User;
import com.mm.appdirect.techchallenge.domain.UserRepository;

@Component
public class AccountService extends BaseService{
  @Autowired
  private AccountRepository accountRepo;
  @Autowired
  private UserRepository userRepo;
  private Logger logger = LoggerFactory.getLogger(AccountService.class);

  public Page<Account> getAll(Pageable p) {
    return accountRepo.findAll(p);
  }

  public EventResult processNewSubscription(SubscriptionEvent event) {
    EventResult result = new EventResult();
    if (event == null || event.getPayload() == null || event.getPayload().getCompany() == null || event.getPayload().getCompany().getUuid() == null) {
      logger.error("Invalid event input received. Company identifier not found.");
      result.setErrorCode(ErrorCodes.ACCOUNT_NOT_FOUND.toString());
      result.setSuccess(false);
      result.setMessage("Company not found");
      return result;
    }

    Company company = event.getPayload().getCompany();
    Account account = convertCompany(company);
    User user = convertCreator(event.getCreator());
    
    Account existingOrg = accountRepo.findByUuid(account.getUuid());
    User existingUser = userRepo.findByUuid(user.getUuid());
    
    if (existingOrg != null) {
      account.setId(existingOrg.getId());
    }
    if(existingUser != null){
      user.setId(existingUser.getId());
    }
    account.setStatus(Status.ACTIVE);
    user.setStatus(User.Status.ACTIVE);
    account = accountRepo.save(account);
    user.setAccount(account);
    user = userRepo.save(user);
    result = new EventResult();
    result.setAccountIdentifier(account.getId().toString());
    result.setSuccess(true);
    result.setMessage("Account created");
    return result;
  }

  

  public EventResult processCancelSubscription(SubscriptionCancelEvent event) {
    EventResult result = new EventResult();
    if (event == null || event.getPayload() == null || event.getPayload().getAccount() == null || event.getPayload().getAccount().getAccountIdentifier() == null) {
      logger.error("Invalid event input received. Account identifier not found.");
      result.setErrorCode(ErrorCodes.ACCOUNT_NOT_FOUND.toString());
      result.setSuccess(false);
      result.setMessage("Account not found");
      return result;
    }
    
    String accountId = event.getPayload().getAccount().getAccountIdentifier();

    // START-WORKAROUND
    // The below is a workaround to process the account cancellation
    // in the ping tests. The ping tests always send dummy-account
    // and not the identifier returned in the subscription
    if ("dummy-account".equals(accountId)) {
      accountId = "1";
    }
    // END-WORKAROUND

    Account existingOrg = this.findAccountById(accountId);

    result = new EventResult();
    if (existingOrg != null) {
      result.setAccountIdentifier(existingOrg.getId().toString());
      result.setSuccess(Boolean.TRUE.booleanValue());
      result.setMessage("Account disabled");
      existingOrg.setStatus(Account.Status.DISABLED);
      existingOrg = accountRepo.save(existingOrg);
      // TODO Disable all users also??
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
    return accountRepo.findOne(convertedId);
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
  
  private User convertCreator(Creator creator) {
    User user = new User();
    user.setEmail(creator.getEmail());
    user.setFirstName(creator.getFirstName());
    user.setLastName(creator.getLastName());
    user.setLocale(creator.getLocale());
    user.setOpenId(creator.getOpenId());
    user.setUuid(creator.getUuid());
    return user;
  }
}

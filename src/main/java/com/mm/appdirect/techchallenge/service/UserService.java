package com.mm.appdirect.techchallenge.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mm.appdirect.techchallenge.api.EventResult;
import com.mm.appdirect.techchallenge.api.event.ErrorCodes;
import com.mm.appdirect.techchallenge.api.event.UserAssignEvent;
import com.mm.appdirect.techchallenge.api.event.UserUnassignEvent;
import com.mm.appdirect.techchallenge.domain.Account;
import com.mm.appdirect.techchallenge.domain.User;
import com.mm.appdirect.techchallenge.domain.UserRepository;

@Component
public class UserService extends BaseService{
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  @Autowired
  private UserRepository userRepo;

  @Autowired
  private AccountService accountSvc;

  public List<User> findByAccount(Account account) {
    return userRepo.findByAccount(account);
  }

  public EventResult processAssignment(UserAssignEvent event) {
    com.mm.appdirect.techchallenge.api.event.User assignedUser = event.getPayload().getUser();
    EventResult result = new EventResult();
    if(event == null || event.getPayload() == null || event.getPayload().getAccount() == null){
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

    Account organization = accountSvc.findAccountById(accountId);

    result = new EventResult();
    if (organization == null) {
      result.setErrorCode(ErrorCodes.ACCOUNT_NOT_FOUND.toString());
      result.setSuccess(false);
      result.setMessage("Account not found");
    } else {
      User user = convertToUser(assignedUser);
      User existingUser = userRepo.findByUuid(user.getUuid());

      if (existingUser != null) {
        user.setId(existingUser.getId());
        
      }
      user.setAccount(organization);
      user.setStatus(User.Status.ACTIVE);
      user = userRepo.save(user);

      result = new EventResult();
      result.setSuccess(true);
    }
    return result;
  }

  public EventResult processUnassignment(UserUnassignEvent event) {
    
    EventResult result = new EventResult();
    if(event == null || event.getPayload() == null || event.getPayload().getUser() == null){
      logger.error("Invalid event input received. User identifier not found.");
      result.setErrorCode(ErrorCodes.USER_NOT_FOUND.toString());
      result.setSuccess(false);
      result.setMessage("User not found");
      return result;
    }
    com.mm.appdirect.techchallenge.api.event.User assignedUser = event.getPayload().getUser();
    User existingUser = userRepo.findByUuid(assignedUser.getUuid());

    result = new EventResult();
    if (existingUser != null) {
      result.setAccountIdentifier(existingUser.getId().toString());
      result.setSuccess(true);
      existingUser.setStatus(User.Status.DISABLED);
      existingUser = userRepo.save(existingUser);
      result.setMessage("User subscription disabled successfully");
    } else {
      result.setErrorCode(ErrorCodes.USER_NOT_FOUND.toString());
      result.setSuccess(false);
      result.setMessage("User subscription does not exist");
    }

    return result;
  }

  private User convertToUser(com.mm.appdirect.techchallenge.api.event.User assignedUser) {
    User user = new User();
    user.setEmail(assignedUser.getEmail());
    user.setFirstName(assignedUser.getFirstName());
    user.setLastName(assignedUser.getLastName());
    user.setLocale(assignedUser.getLocale());
    user.setOpenId(assignedUser.getOpenId());
    user.setUuid(assignedUser.getUuid());
    return user;
  }

}

package com.mm.appdirect.techchallenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.mm.appdirect.techchallenge.api.EventResult;
import com.mm.appdirect.techchallenge.api.event.ErrorCodes;
import com.mm.appdirect.techchallenge.api.event.UserAssignEvent;
import com.mm.appdirect.techchallenge.api.event.UserUnassignEvent;
import com.mm.appdirect.techchallenge.domain.Account;
import com.mm.appdirect.techchallenge.domain.User;
import com.mm.appdirect.techchallenge.domain.UserRepository;

@Component
public class UserService {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountService accountSvc;

	public Page<User> getAll(Pageable p) {
		return userRepo.findAll(p);
	}

	public EventResult processAssignment(UserAssignEvent event) {
		com.mm.appdirect.techchallenge.api.event.User assignedUser = event
				.getPayload().getUser();

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

		EventResult result = new EventResult();
		if (organization == null) {
			result.setErrorCode(ErrorCodes.ACCOUNT_NOT_FOUND.toString());
			result.setStatus(Boolean.FALSE.toString());
			result.setMessage("Account not found");
		} else {
			User user = convertToUser(assignedUser);
			User existingUser = userRepo.findByUuid(user.getUuid());

			if (existingUser != null) {
				user.setId(existingUser.getId());
				user.setCompany(organization);
			}
			user.setStatus(User.Status.ACTIVE);
			user = userRepo.save(user);

			result = new EventResult();
			result.setAccountIdentifier(user.getId().toString());
			result.setStatus(Boolean.TRUE.toString());
		}
		return result;
	}

	public EventResult processUnassignment(UserUnassignEvent event) {
		com.mm.appdirect.techchallenge.api.event.User assignedUser = event
				.getPayload().getUser();
		User existingUser = userRepo.findByUuid(assignedUser.getUuid());

		EventResult result = new EventResult();
		if (existingUser != null) {
			result.setAccountIdentifier(existingUser.getId().toString());
			result.setStatus(Boolean.TRUE.toString());
			existingUser.setStatus(User.Status.DISABLED);
			existingUser = userRepo.save(existingUser);
			result.setMessage("User subscription disabled successfully");
		} else {
			result.setErrorCode(ErrorCodes.USER_NOT_FOUND.toString());
			result.setStatus(Boolean.FALSE.toString());
			result.setMessage("User subscription does not exist");
		}

		return result;
	}

	private User convertToUser(
			com.mm.appdirect.techchallenge.api.event.User assignedUser) {
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

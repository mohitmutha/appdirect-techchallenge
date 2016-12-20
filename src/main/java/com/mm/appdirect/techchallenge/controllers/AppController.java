package com.mm.appdirect.techchallenge.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mm.appdirect.techchallenge.domain.Account;
import com.mm.appdirect.techchallenge.domain.User;
import com.mm.appdirect.techchallenge.service.AccountService;

@RestController
public class AppController {
    
	@Autowired
	private AccountService accountSvc;
    
    @RequestMapping("/accounts")
    public @ResponseBody Page<Account> getAccounts(Pageable page) {
        return accountSvc.getAll(page);
    }
    
    @RequestMapping("/accounts/{accountid}")
    public Account getAccount(@PathVariable(name="accountid") String accountid) {
        return accountSvc.findAccountById(accountid);
    }
    
    @RequestMapping("/accounts/{accountid}/users")
    public List<User> getAccountUsers(@PathVariable(name="accountid") String accountid) {
    	Account account = accountSvc.findAccountById(accountid);
    	return account.getUsers();
    }

}

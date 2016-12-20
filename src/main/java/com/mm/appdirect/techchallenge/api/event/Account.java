package com.mm.appdirect.techchallenge.api.event;

public class Account {
	public enum AccountStatus {
		ACTIVE
	}
	private String accountIdentifier;
    private AccountStatus status;
	public String getAccountIdentifier() {
		return accountIdentifier;
	}
	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
}

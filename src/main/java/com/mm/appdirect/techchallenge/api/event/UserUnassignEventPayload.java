package com.mm.appdirect.techchallenge.api.event;

public class UserUnassignEventPayload {
	private Account account;
	private User user;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

package com.mm.appdirect.techchallenge.api.event;

public class UserAssignEvent extends Event{
	private UserAssignEventPayload payload;

	public UserAssignEventPayload getPayload() {
		return payload;
	}

	public void setPayload(UserAssignEventPayload payload) {
		this.payload = payload;
	}
}

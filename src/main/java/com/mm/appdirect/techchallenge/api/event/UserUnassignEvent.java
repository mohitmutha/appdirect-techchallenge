package com.mm.appdirect.techchallenge.api.event;

public class UserUnassignEvent extends Event{
	private UserAssignEventPayload payload;

	public UserAssignEventPayload getPayload() {
		return payload;
	}

	public void setPayload(UserAssignEventPayload payload) {
		this.payload = payload;
	}
}

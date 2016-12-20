package com.mm.appdirect.techchallenge.api.event;

public class SubscriptionCancelEvent extends Event{
	private SubscriptionCancelEventPayload payload;

	public SubscriptionCancelEventPayload getPayload() {
		return payload;
	}

	public void setPayload(SubscriptionCancelEventPayload payload) {
		this.payload = payload;
	}

}

package com.mm.appdirect.techchallenge.api.event;



public class SubscriptionEvent extends Event{
	private SubscriptionEventPayload payload;

	public SubscriptionEventPayload getPayload() {
		return payload;
	}

	public void setPayload(SubscriptionEventPayload payload) {
		this.payload = payload;
	}
}

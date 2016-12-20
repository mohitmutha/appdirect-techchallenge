package com.mm.appdirect.techchallenge.api;

public class EventWrapper {
	private EventResult result;
	
	public EventWrapper(EventResult result){
		this.result = result;
	}
	
	public EventResult getResult() {
		return result;
	}

	public void setResult(EventResult result) {
		this.result = result;
	}
	
}

package com.mm.appdirect.techchallenge.api.event;

public abstract class Event {
	private EventType type;
	private MarketPlace marketplace;
	private Creator creator;
	private String applicationUuid;
	private String flag;
	
	
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public MarketPlace getMarketplace() {
		return marketplace;
	}
	public void setMarketplace(MarketPlace marketplace) {
		this.marketplace = marketplace;
	}
	public Creator getCreator() {
		return creator;
	}
	public void setCreator(Creator creator) {
		this.creator = creator;
	}
	public String getApplicationUuid() {
		return applicationUuid;
	}
	public void setApplicationUuid(String applicationUuid) {
		this.applicationUuid = applicationUuid;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}

package com.mm.appdirect.techchallenge.api.event;

import java.util.List;


public class Order {
	private String editionCode;
	private List<OrderItem> items;
	public String getEditionCode() {
		return editionCode;
	}
	public void setEditionCode(String editionCode) {
		this.editionCode = editionCode;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
}

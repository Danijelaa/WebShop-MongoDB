package com.webshop.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCartDTOOut{
	
	private String date;
	private List<CartItemDTOOut> items=new ArrayList<>();
	private Double amount;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<CartItemDTOOut> getItems() {
		return items;
	}
	public void setItems(List<CartItemDTOOut> items) {
		this.items = items;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}

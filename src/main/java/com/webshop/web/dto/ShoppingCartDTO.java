package com.webshop.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.webshop.model.CartItem;


public class ShoppingCartDTO {

	private List<CartItemDTO> items=new ArrayList<>();
	
	public List<CartItemDTO> getItems() {
		return items;
	}

	

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}


	
}

package com.webshop.support;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.CartItem;
import com.webshop.web.dto.CartItemDTO;

@Component
public class CartItemDTOToCartItem implements Converter<CartItemDTO, CartItem>{

	@Override
	public CartItem convert(CartItemDTO cartItemDto) {
		CartItem cartItem=new CartItem();
		cartItem.setProduct_id(new ObjectId(cartItemDto.getProduct_id()));
		cartItem.setQuantity(cartItemDto.getQuantity());
		return cartItem;
	}
	
	public List<CartItem> convert(List<CartItemDTO> cartItemDtos){
		List<CartItem> cartItems=new ArrayList<>();
		for(CartItemDTO ciDto:cartItemDtos){
			cartItems.add(convert(ciDto));
		}
		return cartItems;
	}

}

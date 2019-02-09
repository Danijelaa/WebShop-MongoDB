package com.webshop.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.CartItem;
import com.webshop.model.Product;
import com.webshop.service.ProductService;
import com.webshop.web.dto.CartItemDTOOut;

@Component
public class CartItemToCartItemDTOOut implements Converter<CartItem,CartItemDTOOut>{

	@Autowired
	ProductService ps;
	
	@Override
	public CartItemDTOOut convert(CartItem cartItem) {
		CartItemDTOOut cartItemDtoOut=new CartItemDTOOut();
		Product product=ps.getById(cartItem.getProduct_id());
		//System.out.println(product);
		cartItemDtoOut.setProduct_price(product.getPrice());
		cartItemDtoOut.setProduct_title(product.getTitle());
		cartItemDtoOut.setQuantity(cartItem.getQuantity());
		return cartItemDtoOut;
	}

	public List<CartItemDTOOut> convert(List<CartItem> cartItems){
		List<CartItemDTOOut> cartItemDtoOuts=new ArrayList<CartItemDTOOut>();
		for(CartItem ci:cartItems){
			cartItemDtoOuts.add(convert(ci));
		}
		return cartItemDtoOuts;
	}
}

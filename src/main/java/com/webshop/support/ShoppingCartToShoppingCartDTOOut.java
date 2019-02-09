package com.webshop.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.ShoppingCart;
import com.webshop.web.dto.ShoppingCartDTOOut;

@Component
public class ShoppingCartToShoppingCartDTOOut implements Converter<ShoppingCart, ShoppingCartDTOOut>{

	@Autowired
	CartItemToCartItemDTOOut toCartItemDtoOut;
	
	@Override
	public ShoppingCartDTOOut convert(ShoppingCart shoppingCart) {
		ShoppingCartDTOOut shoppingCartDtoOut=new ShoppingCartDTOOut();
		shoppingCartDtoOut.setAmount(shoppingCart.getAmount());
		SimpleDateFormat format=new SimpleDateFormat("dd.MM.yyyy HH:mm");
		shoppingCartDtoOut.setDate(format.format(shoppingCart.getDate()));
		shoppingCartDtoOut.setItems(toCartItemDtoOut.convert(shoppingCart.getItems()));
		return shoppingCartDtoOut;
	}
	
	public List<ShoppingCartDTOOut> convert(List<ShoppingCart> shoppingCarts){
		List<ShoppingCartDTOOut> shoppingCarzDtoOuts=new ArrayList<ShoppingCartDTOOut>();
		for(ShoppingCart sc:shoppingCarts){
			shoppingCarzDtoOuts.add(convert(sc));
		}
		return shoppingCarzDtoOuts;
	}

}

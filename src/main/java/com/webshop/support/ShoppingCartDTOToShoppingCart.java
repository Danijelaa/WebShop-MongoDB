package com.webshop.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.CartItem;
import com.webshop.model.ShoppingCart;
import com.webshop.service.ProductService;
import com.webshop.web.dto.CartItemDTO;
import com.webshop.web.dto.ShoppingCartDTO;

@Component
public class ShoppingCartDTOToShoppingCart implements Converter<List<CartItemDTO>, ShoppingCart>{

	@Autowired
	ProductService ps;
	@Autowired
	CartItemDTOToCartItem toCartItem;
	
	@Override
	public ShoppingCart convert(List<CartItemDTO> itemsDto) {
		 ShoppingCart shoppingCart=new ShoppingCart();
		 List<CartItem> items=toCartItem.convert(itemsDto);
		 shoppingCart.setItems(items);
		return shoppingCart;
	}

}

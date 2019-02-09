package com.webshop.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.webshop.model.CartItem;
import com.webshop.web.dto.CartItemDTO;
import com.webshop.web.dto.ProductDTO;

@Component
public class Validation {

	public boolean validateUpdatedProduct(ProductDTO productDto) {
		if(productDto.getId()==null 
				||	/*productDto.getCategory()==null*/ productDto.getCategory_id()==null /*|| productDto.getCategory().trim().equals("")*/
				|| productDto.getPrice()==null || productDto.getPrice()<=0
				|| productDto.getQuantity()==null || productDto.getQuantity()<0) {
			return false;
		}
		return true;
	}
	
	public boolean validateNewProduct(ProductDTO newProduct) {
		if(newProduct.getTitle()==null || newProduct.getTitle().trim().equals("")
				|| newProduct./*getCategory()*/getCategory_id()==null /*|| newProduct.getCategory().trim().equals("")*/
				|| newProduct.getPrice()==null || newProduct.getPrice()<=0
				|| newProduct.getQuantity()==null || newProduct.getQuantity()<0) {
			return false;
		}
		return true;
	}

	public boolean validateCartItems(List<CartItemDTO> cartItems){
		for(CartItemDTO ci:cartItems){
			if(ci.getProduct_id()==null || ci.getProduct_id().trim().equals("")
					|| ci.getQuantity()==null
					|| ci.getQuantity()<0){
				return false;
			}
		}
		return true;
	}
	
	public List<CartItemDTO> removeDuplicateItems(List<CartItemDTO> cartItems){
		/*for(CartItemDTO ci:cartItems){
			System.out.println(ci.getProduct_id()+" | "+ci.getQuantity());
			
		}
		System.out.println("***********");*/
		List<CartItemDTO> cleanList=new ArrayList<>();
		for(int i=0; i<cartItems.size(); i++){
			boolean exists=false;
			int n=-1;
			for(int j=0; j<cleanList.size(); j++){
				if(cartItems.get(i).getProduct_id().toString().equals(cleanList.get(j).getProduct_id())){
					exists=true;
					n=j;
					break;
				}
			}
			if(exists){
				int quantity=cleanList.get(n).getQuantity();
				quantity+=cartItems.get(i).getQuantity();
				cleanList.get(n).setQuantity(quantity);
			}
			else{
				cleanList.add(cartItems.get(i));
			}
		}
		/*for(CartItemDTO ci:cleanList){
			System.out.println(ci.getProduct_id()+" | "+ci.getQuantity());
		}*/
		return cleanList;
	}
}

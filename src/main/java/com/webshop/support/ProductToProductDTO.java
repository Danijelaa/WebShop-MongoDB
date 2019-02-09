package com.webshop.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.Category;
import com.webshop.model.Product;
import com.webshop.service.CategoryService;
import com.webshop.web.dto.ProductDTO;

@Component
public class ProductToProductDTO implements Converter<Product, ProductDTO>{

	@Autowired
	private CategoryService categoryService;
	
	@Override
	public ProductDTO convert(Product product) {
		ProductDTO productDto=new ProductDTO();
		//productDto.setCategory(product.getCategory());
		productDto.setCategory_id(product.getCategory_id().toString());
		Category category=categoryService.findById(product.getCategory_id());
		productDto.setCategory_title(category.getTitle());
		
		productDto.setId(product.getId().toString());
		productDto.setPrice(product.getPrice());
		productDto.setQuantity(product.getQuantity());
		productDto.setTitle(product.getTitle());
		return productDto;
	}

	public List<ProductDTO> convert(List<Product> products){
		List<ProductDTO> productDtos=new ArrayList<>();
		for(Product p:products) {
			productDtos.add(convert(p));
		}
		return productDtos;
	}
}

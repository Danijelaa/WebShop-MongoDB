package com.webshop.support;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.Category;
import com.webshop.model.Product;
import com.webshop.service.CategoryService;
import com.webshop.service.ProductService;
import com.webshop.web.dto.ProductDTO;

@Component
public class ProductDTOToProduct implements Converter<ProductDTO, Product> {

	
	@Autowired
	private ProductService ps;
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public Product convert(ProductDTO productDto) {
		Product product;
		if(productDto.getId()==null) {
			product=new Product();
			product.setId(new ObjectId());
			product.setTitle(productDto.getTitle());
		}
		else {
			product=ps.getById(new ObjectId(productDto.getId()));
			if(product==null){
				throw new IllegalArgumentException("Can not update non-existent product.");
			}
		}
		//product.setCategory(productDto.getCategory());
		Category category=categoryService.findById(new ObjectId(productDto.getCategory_id()));
		if(category==null){
			throw new IllegalArgumentException("Can not set non-existent category.");
		}
		product.setCategory_id(category.getId());
		
		product.setPrice(productDto.getPrice());
		product.setQuantity(productDto.getQuantity());
		return product;
	}

}

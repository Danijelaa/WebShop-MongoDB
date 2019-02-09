package com.webshop.support;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.Category;
import com.webshop.service.CategoryService;
import com.webshop.web.dto.CategoryDTO;

@Component
public class CategoryDTOToCategory implements Converter<CategoryDTO, Category>{

	@Autowired
	private CategoryService categoryService;
	
	@Override
	public Category convert(CategoryDTO categoryDto) {
		Category category;
		if(categoryDto.getId()==null){
			category=new Category();
			category.setId(new ObjectId());
		}
		else{
			category=categoryService.findById(new ObjectId(categoryDto.getId()));
			if(category==null){
				throw new IllegalArgumentException("Can not update non-existent category.");
			}
		}
		category.setTitle(categoryDto.getTitle());
		return category;
	}

}

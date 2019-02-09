package com.webshop.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.webshop.model.Category;
import com.webshop.web.dto.CategoryDTO;

@Component
public class CategoryToCategoryDTO implements Converter<Category, CategoryDTO>{

	@Override
	public CategoryDTO convert(Category category) {
		CategoryDTO categoryDto=new CategoryDTO();
		categoryDto.setId(category.getId().toString());
		categoryDto.setTitle(category.getTitle());
		return categoryDto;
	}

	public List<CategoryDTO> convert(List<Category> categories){
		List<CategoryDTO> categoryDtos=new ArrayList<>();
		for(Category c:categories){
			categoryDtos.add(convert(c));
		}
		return categoryDtos;
	}
}

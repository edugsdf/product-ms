package com.gmail.edugsdf.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.gmail.edugsdf.models.Product;


public interface ProductService {
	
	public List<Product> findAll();
	public List<Product> findWithFilter(String text, BigDecimal min_price, BigDecimal max_price);
	public Optional<Product> find(Long id);
	public Product create(Product product);
	public Optional<Product> update(Long id, Product product);
	public void delete(Long id);	

}

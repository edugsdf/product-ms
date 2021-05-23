package com.gmail.edugsdf.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmail.edugsdf.models.Product;
import com.gmail.edugsdf.models.QProduct;
import com.gmail.edugsdf.repository.ProductRepository;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class ProductServiceImpl implements ProductService{
	
    @PersistenceContext
    private EntityManager em;	

	@Autowired
	private ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository repo) {
		this.productRepository = repo;
	}	
	
	@Override
	public List<Product> findAll() {
		return this.productRepository.findAll();
	}
	
	@Override
	public List<Product> findWithFilter(String text, BigDecimal min_price, BigDecimal max_price) {
		final JPAQuery<Product> query = new JPAQuery<>(em);
		final QProduct product = QProduct.product;
		
		query.from(product);
		
		if(min_price != null) {
			query.getMetadata().addWhere(product.price.goe(min_price));
		}
		if(max_price != null) {
			query.getMetadata().addWhere(product.price.loe(max_price));
		}
		if( text != null && !"".equals(text)) {
			query.getMetadata().addWhere(product.name.contains(text).or(product.description.contains(text)));	
		}		
		return query.fetch();
	}	

	@Override
	public Product create(Product product) {
		this.productRepository.save(product);
		return product;
	}

	@Override
	public Optional<Product> find(Long id) {
		return this.productRepository.findById(id);
	}
	
	@Override
	public Optional<Product> update(Long id, Product product) {
		return this.productRepository.findById(id).map(data -> {
			data.setName(product.getName());
			data.setDescription(product.getDescription());
			data.setPrice(product.getPrice());
			this.productRepository.save(data);
			return data;
		});
	}

	@Override
	public void delete(Long id) {
		this.productRepository.findById(id).map(data -> {
			if(data != null) {
				this.productRepository.delete(data);
			}
			return data;
		});		
	}
	
}

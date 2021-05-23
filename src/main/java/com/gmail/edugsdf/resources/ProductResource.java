package com.gmail.edugsdf.resources;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.edugsdf.exceptions.ProductNotFundException;
import com.gmail.edugsdf.models.Product;
import com.gmail.edugsdf.services.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "API REST - Model Product")
@RestController
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductService productService;
	
	public ProductResource(ProductService service) {
		this.productService = service;
	}	
	
	@ApiOperation(value = "Find all products in database ")
	//@GetMapping(produces = "application/json")
	@GetMapping()
	public @ResponseBody ResponseEntity<?> findAll() {
		List<Product> list = productService.findAll();
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Find products by id in database ")
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		Optional<Product> productFind = this.productService.find(id);
		if(!productFind.isPresent()) {
			return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(productFind.get(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Find products by filter")
	@RequestMapping(value="search", method = RequestMethod.GET)
	@ResponseBody	
	public ResponseEntity<?> findWithFilter(@RequestParam Map<String,String> allParams) {
		String nameOrDescription = allParams.get("q") != null ? allParams.get("q") : "";
		BigDecimal min_price = allParams.get("min_price") != null ? new BigDecimal(allParams.get("min_price")) : null;
		BigDecimal max_price = allParams.get("max_price") != null ? new BigDecimal(allParams.get("max_price")) : null;
		
		List<Product> list = productService.findWithFilter(nameOrDescription, min_price, max_price);
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "create new product in database ")
	@PostMapping()
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Product product) {
			Product created = this.productService.create(product);
			return new ResponseEntity<Product>(created, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "update a product in database ")
	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody Product product) {
		Optional<Product> productReturn = productService.update(id, product);
		if(!productReturn.isPresent()) {
			throw new ProductNotFundException("Product not fund.");
		}
		return new ResponseEntity<Product>(productReturn.get(), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "delete a product in database, by id")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) {
		this.productService.delete(id);
	}	
	
	
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.put("status_code", 400);
            errors.put("message", errorMessage);
        });
        return errors;
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFundException.class)
    public Map<String, Object> handleProductNotFundExceptions(ProductNotFundException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status_code", 404);
        errors.put("message", ex.getMessage());
        return errors;
    }    

}

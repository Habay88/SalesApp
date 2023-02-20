package com.mintyn.service;

import java.util.List;

import com.mintyn.entity.Product;
import com.mintyn.model.ProductRequest;
import com.mintyn.model.ProductResponse;

public interface ProductService {

	long addProduct(ProductRequest productRequest);

	ProductResponse getProductById(long productId);

	List<Product> getAllProducts();

	
void reduceQuantity(long productId, long quantity);

Product updateProduct(Product productResponse);

}

package com.mintyn.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mintyn.entity.Product;
import com.mintyn.exception.ProductServiceCustomException;
import com.mintyn.model.ProductRequest;
import com.mintyn.model.ProductResponse;
import com.mintyn.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {
    @Autowired
	ProductRepository productRepository;


	@Override
	public long addProduct(ProductRequest productRequest) {
		log.info("adding products.....");
		Product product = Product.builder()
				.productName(productRequest.getName())
			    .quantity(productRequest.getQuantity())
			    .price(productRequest.getPrice())
			    .build();
		productRepository.save(product);
		log.info("product created");
		return product.getProductId();
	}


	@Override
	public ProductResponse getProductById(long productId) {
		// TODO Auto-generated method stub
		log.info("Get the product for productid {}", productId);
		Product product = productRepository.findById(productId)
				.orElseThrow(
				    	() -> new ProductServiceCustomException	("product with given id not found", "PRODUCT NOT FOUND"));

		ProductResponse productResponse =new ProductResponse();
		BeanUtils.copyProperties(product, productResponse);
	return productResponse;
	}
//
//	public long addProduct(ProductRequest productRequest) {
//		log.info("adding products.....");
//		Product product = Product.builder()
//				.productName(productRequest.getName())
//			    .quantity(productRequest.getQuantity())
//			    .price(productRequest.getPrice())
//			    .build();
//		productRepository.save(product);
//		log.info("product created");
//		return product.getProductId();
//	}



	

	@Override
	public void reduceQuantity(long productId, long quantity) {
		log.info("Reduce Quantity {} for id: {}",quantity,productId);

		Product product  = productRepository.findById(productId)
				.orElseThrow(() -> new ProductServiceCustomException(
						"Product with given id not found",
						"PRODUCT NOT FOUND"
						));
						 if(product.getQuantity() < quantity) {
					            throw new ProductServiceCustomException(
					                    "Product does not have sufficient Quantity",
					                    "INSUFFICIENT_QUANTITY"
					            );
					        }
						 product.setQuantity(product.getQuantity() - quantity);
						 productRepository.save(product);
						  log.info("Product Quantity updated Successfully");
		}


	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return(List<Product>) productRepository.findAll();
	}


	@Override
	public Product updateProduct(Product productResponse) {
		
		Product update = productRepository.findById(productResponse.getProductId()).get();
		update.setPrice(productResponse.getPrice());
		update.setProductName(productResponse.getProductName());
		update.setQuantity(productResponse.getQuantity());
		
		Product updatedProduct = productRepository.save(update);
		
		return updatedProduct;
	}


	





	}



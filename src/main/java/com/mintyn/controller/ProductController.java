package com.mintyn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mintyn.entity.Product;
import com.mintyn.model.ProductRequest;
import com.mintyn.model.ProductResponse;
import com.mintyn.service.ProductService;




@RestController("")
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;
    @PostMapping
	public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
    	long productId = productService.addProduct(productRequest);
		return new ResponseEntity<>(productId,HttpStatus.CREATED);

	}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct( @RequestBody Product product,@PathVariable("id") long productId){
    	product.setProductId(productId);
        Product updatedProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productId){
		ProductResponse productResponse   = productService.getProductById(productId);
		return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());

    }
}



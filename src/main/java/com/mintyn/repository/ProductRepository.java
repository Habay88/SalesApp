package com.mintyn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mintyn.entity.Product;
import com.mintyn.model.ProductResponse;




@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	List<Product> findAll();

}

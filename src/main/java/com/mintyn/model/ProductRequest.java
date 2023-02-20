package com.mintyn.model;

import lombok.Data;

@Data
public class ProductRequest {

	private String name;
	private Long price;
	private int quantity;

}

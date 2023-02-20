package com.mintyn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mintyn.entity.Order;
import com.mintyn.model.OrderRequest;
import com.mintyn.model.OrderResponse;
import com.mintyn.producer.OrderEventProducer;
import com.mintyn.service.OrderService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/order" )
@Log4j2
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	OrderEventProducer orderEventProducer;

	@PostMapping("/placeOrder")
	public ResponseEntity<Long> placeOrder(@Valid  @RequestBody OrderRequest orderRequest){
	long orderId = orderService.placeOrder(orderRequest);
	log.info("Order id: {}", orderId);
	return new ResponseEntity<>(orderId,HttpStatus.OK);

	}
	
	@PostMapping("/placeOrders")
    public ResponseEntity<Order> postOrderEvent(@Valid @RequestBody  Order orderEvent) throws JsonProcessingException  {

		orderEventProducer.sendLibraryEvent(orderEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderEvent);
    }

	@GetMapping("/{orderId}")
   public ResponseEntity<OrderResponse>	getOrderDetails(@PathVariable long orderId){

		OrderResponse orderResponse = orderService.getOrderDetails(orderId);
	return new ResponseEntity<>(orderResponse,HttpStatus.OK);
	}

}

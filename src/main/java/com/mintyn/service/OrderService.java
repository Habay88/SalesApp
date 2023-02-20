package com.mintyn.service;

import com.mintyn.model.OrderRequest;
import com.mintyn.model.OrderResponse;

public interface OrderService {

	long placeOrder(OrderRequest orderRequest);

	OrderResponse getOrderDetails(long orderId);


}

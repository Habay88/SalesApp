package com.mintyn.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mintyn.entity.Order;
import com.mintyn.exception.CustomException;
import com.mintyn.model.OrderRequest;
import com.mintyn.model.OrderResponse;
import com.mintyn.model.ProductResponse;
import com.mintyn.repository.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;



    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {

        //Order Entity -> Save the data with Status Order Created
        //Product Service - Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success-> COMPLETE, Else
        //CANCELLED

        log.info("Placing Order Request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

     //   log.info("Calling Payment Service to complete the payment");
//        PaymentRequest paymentRequest
//                = PaymentRequest.builder()
//                .orderId(order.getId())
//                .paymentMode(orderRequest.getPaymentMode())
//                .amount(orderRequest.getTotalAmount())
//                .build();
//
       String orderStatus = null;
//        try {
//            paymentService.doPayment(paymentRequest);
//            log.info("Payment done Successfully. Changing the Oder status to PLACED");
//            orderStatus = "PLACED";
//        } catch (Exception e) {
//            log.error("Error occurred in payment. Changing order status to PAYMENT_FAILED");
//            orderStatus = "PAYMENT_FAILED";
//        }

       order.setOrderStatus(orderStatus);
      orderRepository.save(order);

        log.info("Order Places successfully with Order Id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id : {}", orderId);

        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the order Id:" + orderId,
                        "NOT_FOUND",
                        404));

        log.info("Invoking Product service to fetch the product for id: {}", order.getProductId());
        ProductResponse productResponse
                = restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class
        );



        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();



        OrderResponse orderResponse
                = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())


                .build();

        return orderResponse;
    }

}

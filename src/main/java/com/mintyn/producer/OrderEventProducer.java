package com.mintyn.producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mintyn.entity.Order;
import com.mintyn.model.OrderRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class OrderEventProducer {

    @Autowired
    KafkaTemplate<Long,String> kafkaTemplate;

    String topic = "order-event";
    @Autowired
    ObjectMapper objectMapper;

    @SuppressWarnings("deprecation")
	public void sendLibraryEvent(Order orderEvent) throws JsonProcessingException  {

    	Long key = orderEvent.getId();
    	String value = objectMapper.writeValueAsString(orderEvent);
   @SuppressWarnings({ "unchecked" })
ListenableFuture<SendResult<Long,String>> listenableFuture = (ListenableFuture<SendResult<Long, String>>) kafkaTemplate.sendDefault(key,value);
   listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long,String>>(){

	@Override
	public void onSuccess(SendResult<Long, String> result) {
		// TODO Auto-generated method stub
		handleSuccess(key,value,result);
	}

	private void handleSuccess(Long key, String value, SendResult<Long, String> result) {
		log.info("Message sent successfully for the key: {} and the value is {}, partition is {}", key, value, result.getRecordMetadata().partition());
		
	}

	@Override
	public void onFailure(Throwable ex) {
	handleFailure(key,value,ex);
		
	}

	private void handleFailure(Long key, String value, Throwable ex) {
	log.error("error sending the message and the exception is {}", ex.getMessage());
	try{
		throw ex;
	} catch(Throwable throwable) {
		throwable.printStackTrace();
		log.error("Error in OnFailure; {}", throwable.getMessage());
	}
	}
	
	   
   });
}
}
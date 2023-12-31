package com.nhom3.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom3.dao.OrderDAO;
import com.nhom3.dao.OrderDetailDAO;
import com.nhom3.entity.Order;
import com.nhom3.entity.OrderDetail;
import com.nhom3.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderDAO odao;
	
	@Autowired
	OrderDetailDAO ddao;

	@Override
	public Order create(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.convertValue(orderData,  Order.class);
		odao.save(order);
		
		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {
		};
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type)
		.stream().peek(d -> d.setOrder(order)).collect(Collectors.toList());
		ddao.saveAll(details);
		return order;
	}

	@Override
	public Order findById(Long id) {
		return odao.findById(id).get();
	}

	@Override
	public List<Order> findByUsername(String username) {
		return odao.findByUsername(username);
	}

}

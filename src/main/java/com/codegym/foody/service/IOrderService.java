package com.codegym.foody.service;

import com.codegym.foody.model.Order;
import com.codegym.foody.model.OrderItem;
import com.codegym.foody.model.enumable.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {
    Page<Order> searchOrders(String keyword, Long restaurantId, Pageable pageable);
    Order getOrderById(Long id);
    Order updateOrderStatus(Long id, OrderStatus status);
    void cancelOrder(Long id);
    void saveOrderItem(OrderItem orderItem);
    void saveOrder(Order order);
    List<Order> getOrdersByUsername(String username);
}

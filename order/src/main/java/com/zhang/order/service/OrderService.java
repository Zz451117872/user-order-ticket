package com.zhang.order.service;

import com.zhang.common.dto.OrderDTO;
import com.zhang.order.dao.OrderDao;
import com.zhang.order.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public void createOrder(Order order) {

    }

    public void deleteOne(Integer id) {
        orderDao.deleteById( id );
    }

    public void deleteAll() {
        orderDao.deleteAllInBatch();
    }

    public List<Order> getAll() {
        return orderDao.findAll();
    }

    public OrderDTO selectOne(Integer orderId) {
        Order order = orderDao.getOne( orderId );
        return convertOrderDTO( order );
    }

    private OrderDTO convertOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus( order.getStatus() );
        orderDTO.setAmount( order.getAmount() );
        orderDTO.setRease( order.getRease() );
        orderDTO.setTicketId( order.getTicketId() );
        orderDTO.setUserId( order.getUserId() );
        orderDTO.setId( order.getId() );
        orderDTO.setTitle( order.getTitle() );
        return orderDTO;
    }

    public int updateStatus(Integer orderId, Integer status) {
        return orderDao.updateStatus( orderId ,status );
    }
}

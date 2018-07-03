package com.zhang.order.web;

import com.zhang.common.dto.OrderDTO;
import com.zhang.order.domain.Order;
import com.zhang.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/createOrder")
    @ResponseBody
    public String createOrder( Order order )
    {
        orderService.createOrder( order );
        return "success";
    }

    @RequestMapping("/deleteOne")
    @ResponseBody
    public String deleteOne( Integer id ){
        orderService.deleteOne( id );
        return "success";
    }

    @RequestMapping("/deleteAll")
    @ResponseBody
    public String deleteAll( ){
        orderService.deleteAll();
        return "success";
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public List<Order> getAll(){
        return orderService.getAll();
    }

    @RequestMapping("/selectOne")
    @ResponseBody
    public OrderDTO selectOne( Integer orderId ){
        return orderService.selectOne( orderId );
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST )
    @ResponseBody
    public int updateStatus( Integer orderId , Integer status ){
        return orderService.updateStatus( orderId , status );
    }
}

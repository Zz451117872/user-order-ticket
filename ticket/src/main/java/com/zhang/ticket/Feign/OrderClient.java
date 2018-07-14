package com.zhang.ticket.Feign;

import com.zhang.common.FeignClient.api.OrderClientApi;
import com.zhang.common.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "order" , path = "/order")
public interface OrderClient extends OrderClientApi{


    @GetMapping("/selectOne")
    public OrderDTO selectOne(@RequestParam("id")Integer id);

    @PostMapping("/updateStatus")
    int updateStatus(@RequestParam("orderId")Integer orderId, @RequestParam("status")Integer status);
}

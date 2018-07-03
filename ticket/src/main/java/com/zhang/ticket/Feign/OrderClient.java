package com.zhang.ticket.Feign;

import com.zhang.common.FeignClient.api.OrderClientApi;
import com.zhang.common.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "order" , path = "/order")
public interface OrderClient extends OrderClientApi{

    @Override
    @GetMapping("/selectOne")
    public OrderDTO selectOne(Integer id);

    @Override
    @PostMapping("/updateStatus")
    int updateStatus(Integer orderId, Integer status);
}

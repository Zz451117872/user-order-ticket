package com.zhang.common.FeignClient.api;

import com.zhang.common.dto.OrderDTO;

public interface OrderClientApi {

    public OrderDTO selectOne(Integer orderId );

    public int updateStatus( Integer orderId , Integer status );
}

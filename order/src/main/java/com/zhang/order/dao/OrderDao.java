package com.zhang.order.dao;

import com.zhang.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order,Integer> {

    @Modifying
    @Query("update order1 set status = ? where id = ?")
    int updateStatus( Integer status , Integer orderId );

    @Modifying
    @Query("select * from order1 where status = ? and createTime > ? ")
    List<Order> selectByTimeAndStatus(   Integer status , long timeout );
}

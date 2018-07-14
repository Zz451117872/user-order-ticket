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
    @Query("update order1 set status = ?1 where id = ?2")
    int updateStatus( Integer status , Integer orderId );

    @Modifying
    @Query("select o from order1 as o where status = ?1 and createTime > ?2 ")
    List<Order> selectByTimeAndStatus(   Integer status , long timeout );
}

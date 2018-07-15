package com.zhang.order.service;

import com.google.gson.Gson;
import com.zhang.common.Const;
import com.zhang.common.dto.OrderDTO;
import com.zhang.order.dao.OrderDao;
import com.zhang.order.domain.Order;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class OrderJmsService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Gson gson;

    Logger log = LoggerFactory.getLogger( OrderJmsService.class );

    /**
     * 已锁票消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_locked, containerFactory = "msgFactory")
    public void ticketLockedHandler( OrderDTO orderDTO ){
        log.info( Const.order_locked +"收到消息--" + orderDTO);
        Order order = createOrder( orderDTO );
        orderDao.save( order );
        orderDTO.setId( order.getId() );
        sendObject( Const.order_pay_no , orderDTO , "_type" , OrderDTO.class );
        //jmsTemplate.convertAndSend(Const.order_pay_no , orderDTO);
        log.info( "创建订单成功--"+ orderDTO );
    }

    /**
     * 完成订单消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_done , containerFactory = "msgFactory")
    public void orderFinishHandler( OrderDTO orderDTO )
    {
        log.info( Const.order_done +"收到消息:" + orderDTO);
        Order order = orderDao.getOne( orderDTO.getId() );
        if( order.getStatus() != 2 ){
            log.info( "订单状态不为已提票，无法完成订单--" + orderDTO );
            return;
        }
        order.setStatus( 3 );
        orderDao.save( order );
        log.info( "完成订单--" + orderDTO );
    }

    /**
     * 锁定订单失败消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_lock_fail , containerFactory = "msgFactory")
    public void lockFailHandler( OrderDTO orderDTO )
    {
        log.info( Const.order_lock_fail +"收到消息:" + orderDTO);
        Order order = orderDao.getOne( orderDTO.getId() );
        order.setStatus( 6 );
        order.setRease( "锁票失败");
        orderDao.save( order );
        log.info( "处理锁票失败订单成功--" + orderDTO );
    }

    /**
     * 支付失败消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_pay_fail , containerFactory = "msgFactory")
    public void payFailHandler( OrderDTO orderDTO )
    {
        log.info( Const.order_pay_fail +"收到消息:" + orderDTO);
        Order order = orderDao.getOne( orderDTO.getId() );
        order.setStatus( 10 );
        order.setRease( "支付失败");
        orderDao.save( order );
        log.info( "处理支付失败订单成功--" + orderDTO );
    }

    /**
     * 订单超时消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_timeout , containerFactory = "msgFactory")
    public void orderTimeoutHandler( OrderDTO orderDTO )
    {
        log.info( Const.order_timeout +"收到消息--" + orderDTO);
        Order order = orderDao.getOne( orderDTO.getId() );
        order.setStatus( 9 );
        order.setRease( "订单超时");
        orderDao.save( order );
        log.info( "处理超时订单成功--" + orderDTO );
    }


    /**
     * 失败订单消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_fail , containerFactory = "msgFactory")
    public void failedHandler( OrderDTO orderDTO )
    {
        log.info( Const.order_fail +"收到消息--" + orderDTO);
        Order order = orderDao.getOne( orderDTO.getId() );
        order.setStatus( 7 );
        orderDao.save( order );
        log.info( "处理失败订单成功--" + orderDTO );
    }

    /**
     * 检查超时订单调度器
     */
    @Scheduled(fixedDelay = 10000l)
    public void orderTimeoutCheck(){
        long timeout = ( new Date() ).getTime() - 10 * 60 * 1000 ;
        List<Order> orders = orderDao.selectByTimeAndStatus( 0 , timeout );
        if( orders != null && orders.size() > 0 ){
            for( Order order : orders ){
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setStatus( order.getStatus() );
                orderDTO.setId( order.getId() );
                orderDTO.setTitle( order.getTitle() );
                orderDTO.setUserId( order.getUserId() );
                orderDTO.setTicketId( order.getTicketId() );
                orderDTO.setRease( order.getRease() );
                orderDTO.setAmount( order.getAmount() );
                sendObject( Const.order_timeout , orderDTO , "_type" , OrderDTO.class );
                //jmsTemplate.convertAndSend( Const.order_timeout, orderDTO );
            }
        }
    }
    private Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setAmount( orderDTO.getAmount() );
        order.setTicketId( orderDTO.getTicketId() );
        order.setUserId( orderDTO.getUserId() );
        order.setTitle( orderDTO.getTitle() );
        return order;
    }

    private void sendObject( String dest , Object obj , String typeId , Class clazz ){
        jmsTemplate.send( dest, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();
                message.setStringProperty( typeId , clazz.getName() );
                String str = gson.toJson( obj , clazz );
                message.setText( str );
                return message;
            }
        });
    }
}

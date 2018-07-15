package com.zhang.ticket.service;

import com.google.gson.Gson;
import com.zhang.common.Const;
import com.zhang.common.dto.OrderDTO;
import com.zhang.ticket.Feign.OrderClient;
import com.zhang.ticket.dao.TicketDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class TicketJmsService {

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private Gson gson;

    Logger log = LoggerFactory.getLogger( TicketJmsService.class );

    /**
     * 新订单监听器，处理新订单消息OrderDTO orderDto
     * @param orderDto
     */
    @Transactional
    @JmsListener(destination = Const.order_new , containerFactory = "msgFactory")
    public void ticketJmsServiceHandler( OrderDTO orderDto){
        log.info( Const.order_new +"收到消息:" , orderDto);
        int count = ticketDao.lockTicket( orderDto.getUserId() , orderDto.getTicketId() );
        //为1则表示锁票成功，反之则为锁票失败
        if( count == 1 ){
            log.info( "锁票成功--" , orderDto );
            orderDto.setStatus( 1 );
            sendObject( Const.order_locked , orderDto , "_type" , OrderDTO.class );
            //jmsTemplate.convertAndSend(Const.order_locked, orderDto );
        }else{
            log.info( "锁票失败--" , orderDto );
            orderDto.setStatus( 6 );
            sendObject( Const.order_lock_fail , orderDto , "_type" , OrderDTO.class );
            //jmsTemplate.convertAndSend(Const.order_lock_fail, orderDto );
        }
    }

    /**
     * 提票监听器，处理提票消息
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_ticket_move , containerFactory = "msgFactory")
    public void ticketMoveHandler( OrderDTO orderDTO ){
        log.info( Const.order_ticket_move +"收到消息:" , orderDTO);
        int count = ticketDao.moveTicket( orderDTO.getUserId() , orderDTO.getTicketId() , orderDTO.getUserId() );
        if( count != 1 ){
            log.info( "提票失败--" , orderDTO );
            orderDTO.setStatus( 11 );
            orderClient.updateStatus( orderDTO.getId() , 11 );
            sendObject( Const.order_ticket_movefail , orderDTO , "_type" , OrderDTO.class );
            //jmsTemplate.convertAndSend(Const.order_ticket_movefail, orderDTO );
        }else {
            log.info( "提票成功--" , orderDTO );
            orderDTO.setStatus( 2 );
            orderClient.updateStatus( orderDTO.getId() , 2 );
            sendObject( Const.order_done , orderDTO , "_type" , OrderDTO.class );
            //jmsTemplate.convertAndSend(Const.order_done, orderDTO );
        }
    }

    /**
     * 解锁票监听，处理待解锁消息
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_await_unlock , containerFactory = "msgFactory")
    public void unlockTicketHandler( OrderDTO orderDTO ){
        log.info( Const.order_await_unlock +"收到消息:" , orderDTO);
        int count = ticketDao.unlockTicket( orderDTO.getTicketId() , orderDTO.getUserId() );
        if( count == 1 ){
            log.info( "解锁票成功--" , orderDTO );
            orderDTO.setStatus( 5 );
            orderClient.updateStatus( orderDTO.getId() , 5 );
        }

        count = ticketDao.unmoveTicket( orderDTO.getTicketId() , orderDTO.getUserId() );
        if( count == 1 ){
            log.info( "解绑票成功--" , orderDTO );
            orderDTO.setStatus( 8 );
            orderClient.updateStatus( orderDTO.getId() , 8 );
        }
        sendObject( Const.order_fail , orderDTO , "_type" , OrderDTO.class );
        //jmsTemplate.convertAndSend( Const.order_fail, orderDTO );
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

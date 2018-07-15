package com.zhang.user.service;

import com.google.gson.Gson;
import com.zhang.common.Const;
import com.zhang.common.dto.OrderDTO;
import com.zhang.user.Feign.OrderClient;
import com.zhang.user.dao.CustomerDao;
import com.zhang.user.dao.PayInfoDao;
import com.zhang.user.dao.RefundMoneyLogDao;
import com.zhang.user.domain.Customer;
import com.zhang.user.domain.PayInfo;
import com.zhang.user.domain.RefundMoneyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class UserJmsService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private PayInfoDao payInfoDao;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private RefundMoneyLogDao refundMoneyLogDao;

    @Autowired
    private Gson gson;

    Logger log = LoggerFactory.getLogger( UserJmsService.class );

    /**
     * 未支付订单消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_pay_no, containerFactory = "msgFactory")
    public void userPayHandler( OrderDTO orderDTO){
        log.info( Const.order_pay_no +"收到消息--" + orderDTO);
        PayInfo payInfo = payInfoDao.getOne( orderDTO.getId() );
        if( payInfo != null ){
            log.info( "未支付订单重复消息--" + orderDTO );
            log.info("payinfo -- " + payInfo );
            return ;
        }
        Customer customer = customerDao.getOne( orderDTO.getUserId() );
        if( customer.getAmount() < orderDTO.getAmount() ){
            log.info( "账户余额不足--" + orderDTO );
            sendObject( Const.order_await_unlock , orderDTO , "_type" , OrderDTO.class );
            //jmsTemplate.convertAndSend( Const.order_await_unlock, orderDTO );
            return ;
        }else{
            int result = customerDao.updateAmount( orderDTO.getAmount() , orderDTO.getUserId() , customer.getAmount() );
            if( result > 0 ){
                log.info( "订单支付成功--"+ orderDTO );
                payInfo = new PayInfo();
                payInfo.setAmount( orderDTO.getAmount() );
                payInfo.setOrderId( orderDTO.getId() );
                payInfo.setStatus( 1 );
                payInfoDao.save( payInfo );
                orderDTO.setStatus( 1 );

                orderClient.updateStatus( orderDTO.getId() , 1 );
                sendObject( Const.order_ticket_move , orderDTO , "_type" , OrderDTO.class );
                //jmsTemplate.convertAndSend( Const.order_ticket_move , orderDTO );
            }else{
                sendObject( Const.order_pay_fail , orderDTO , "_type" , OrderDTO.class );
                //jmsTemplate.convertAndSend( Const.order_pay_fail , orderDTO );
                log.info( "执行支付订单失败--" + orderDTO );
            }
        }
    }

    /**
     * 提票失败消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_ticket_movefail, containerFactory = "msgFactory")
    public void moveFailHandler( OrderDTO orderDTO){
        log.info( Const.order_ticket_movefail +"收到消息:" , orderDTO);
        /**
         * 提票失败：先要返还购票费用 ，再发送至待解锁票消息队列
         */
        RefundMoneyLog refundMoneyLog = refundMoneyLogDao.getOne( orderDTO.getId() );
        if( refundMoneyLog != null ){
            log.info("重复退款消息--", orderDTO );
            return ;
        }
        Customer customer = customerDao.getOne( orderDTO.getUserId() );
        if( customer == null ){
            log.info("退款账户不存在--", orderDTO );
            return ;
        }
        int result = customerDao.updateAmount( orderDTO.getAmount() , orderDTO.getUserId() , customer.getAmount() );
        if( result > 0 ){
            log.info("退款成功--", orderDTO );
            refundMoneyLog = new RefundMoneyLog();
            refundMoneyLog.setAmount( orderDTO.getAmount() );
            refundMoneyLog.setOrderId( orderDTO.getId() );
            refundMoneyLog.setUserId( orderDTO.getUserId() );
            refundMoneyLogDao.save( refundMoneyLog );
            sendObject( Const.order_await_unlock , orderDTO , "_type" , OrderDTO.class );
            //jmsTemplate.convertAndSend(Const.order_await_unlock , orderDTO );
        }else{
            log.info("退款失败--", orderDTO );
        }
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

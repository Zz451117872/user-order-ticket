package com.zhang.user.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    Logger log = LoggerFactory.getLogger( UserJmsService.class );

    /**
     * 未支付订单消息队列
     * @param orderDTO
     */
    @Transactional
    @JmsListener(destination = Const.order_pay_no, containerFactory = "msgFactory")
    public void userPayHandler( OrderDTO orderDTO){

        PayInfo payInfo = payInfoDao.getOne( orderDTO.getId() );
        if( payInfo != null ){
            log.debug( "未支付订单重复消息--" , orderDTO );
            return ;
        }
        Customer customer = customerDao.getOne( orderDTO.getUserId() );
        if( customer.getAmount() < orderDTO.getAmount() ){
            log.debug( "账户余额不足--" , orderDTO );
            jmsTemplate.convertAndSend( Const.order_await_unlock, orderDTO );
            return ;
        }else{
            int result = customerDao.updateAmount( orderDTO.getAmount() , orderDTO.getUserId() , customer.getAmount() );
            if( result > 0 ){
                log.debug( "订单支付成功--" , orderDTO );
                payInfo = new PayInfo();
                payInfo.setAmount( orderDTO.getAmount() );
                payInfo.setOrderId( orderDTO.getId() );
                payInfo.setStatus( 1 );
                payInfoDao.save( payInfo );
                orderDTO.setStatus( 1 );

                orderClient.updateStatus( orderDTO.getId() , 1 );
                jmsTemplate.convertAndSend( Const.order_ticket_move , orderDTO );
            }else{
                jmsTemplate.convertAndSend( Const.order_pay_fail , orderDTO );
                log.debug( "执行支付订单失败--" , orderDTO );
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
        /**
         * 提票失败：先要返还购票费用 ，再发送至待解锁票消息队列
         */
        RefundMoneyLog refundMoneyLog = refundMoneyLogDao.getOne( orderDTO.getId() );
        if( refundMoneyLog != null ){
            log.debug("重复退款消息--", orderDTO );
            return ;
        }
        Customer customer = customerDao.getOne( orderDTO.getUserId() );
        if( customer == null ){
            log.debug("退款账户不存在--", orderDTO );
            return ;
        }
        int result = customerDao.updateAmount( orderDTO.getAmount() , orderDTO.getUserId() , customer.getAmount() );
        if( result > 0 ){
            log.debug("退款成功--", orderDTO );
            refundMoneyLog = new RefundMoneyLog();
            refundMoneyLog.setAmount( orderDTO.getAmount() );
            refundMoneyLog.setOrderId( orderDTO.getId() );
            refundMoneyLog.setUserId( orderDTO.getUserId() );
            refundMoneyLogDao.save( refundMoneyLog );
            jmsTemplate.convertAndSend(Const.order_await_unlock , orderDTO );
        }else{
            log.debug("退款失败--", orderDTO );
        }
    }

}

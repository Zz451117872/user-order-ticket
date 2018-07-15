package com.zhang.order.web;

import com.google.gson.Gson;
import com.zhang.common.Const;
import com.zhang.common.dto.OrderDTO;
import com.zhang.order.domain.Order;
import com.zhang.order.service.OrderService;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.*;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Gson gson;

    @RequestMapping("/createOrder")
    public void createOrder( Order order )
    {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAmount( order.getAmount() );
        orderDTO.setTicketId( order.getTicketId() );
        orderDTO.setUserId( order.getUserId() );
        orderDTO.setTitle( order.getTitle() );
        sendObject( Const.order_new , orderDTO ,"_type" , OrderDTO.class );
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

//    @RequestMapping("/read")
//    @ResponseBody
//    public String read(){
//        jmsTemplate.setReceiveTimeout( 2000 );
//        Object obj = jmsTemplate.receiveAndConvert( Const.order_new);
//
//        if( obj != null ){
//            System.out.println("有读取到消息");
//            try{
//                OrderDTO orderDTO = (OrderDTO) obj;
//                System.out.println( "order: "+ orderDTO.t);
//                return orderDTO.getTitle();
//            }catch (Exception e){
//                System.out.println("类型无法完成转换");
//            }
//
//        }else {
//            System.out.println( "没有读到消息");
//        }
//        return "null";
//    }

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
    public OrderDTO selectOne(Integer orderId ){
        return orderService.selectOne( orderId );
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST )
    @ResponseBody
    public int updateStatus( Integer orderId , Integer status ){
        return orderService.updateStatus( orderId , status );
    }
}

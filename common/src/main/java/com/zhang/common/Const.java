package com.zhang.common;

public class Const {

    public static final String order_new = "order:new";
    public static final String order_locked = "order:locked";
    public static final String order_lock_fail = "order:lock:fail";
    public static final String order_pay_no = "order:pay:no";
    public static final String order_pay_fail = "order:pay:fail";
    public static final String order_await_unlock = "order:await:unlock";
    public static final String order_ticket_move = "order:ticket:move";
    public static final String order_ticket_movefail = "order:ticket:movefail";
    public static final String order_done = "order:done";
    public static final String order_timeout = "order:timeout";
    public static final String order_fail = "order:fail";




    // 业务状态
    public enum  OrderStatus{
        NEW(1,"新订单"),
        PAID(2,"已付款"),
        MOVED(3,"已提票"),
        DONE(3,"已完成"),
        REFUNDMONEY(4,"已退款"),
        UNMOVEDED(5,"已解提票"),
        LOCKFAIL(6,"锁票失败"),
        FINISH(7,"已结束"),
        REFUNDTICKET(8,"已退票"),
        TIMEOUT(10,"超时"),
        PAIDFAIL(11,"支付失败"),
        MOVEDFAID(12,"提票失败")
        ;
        private String value;
        private int code;

        OrderStatus(int code,String value)
        {
            this.code = code;
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }

        public static OrderStatus codeof(int code)
        {
            for(OrderStatus orderStatus : values())
            {
                if(orderStatus.getCode() == code)
                {
                    return orderStatus;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }
}

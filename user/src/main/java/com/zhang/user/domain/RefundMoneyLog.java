package com.zhang.user.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "refund_money_log")
public class RefundMoneyLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column
    private int amount;

    @Column
    private int userId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RefundMoneyLog{" +
                "orderId=" + orderId +
                ", amount=" + amount +
                ", userId=" + userId +
                '}';
    }
}

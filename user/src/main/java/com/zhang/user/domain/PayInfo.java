package com.zhang.user.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "payinfo")
public class PayInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int orderId;

    @Column
    private int status;

    @Column
    private int amount;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", status=" + status +
                ", amount=" + amount +
                '}';
    }
}

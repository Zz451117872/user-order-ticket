package com.zhang.order.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "order1")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private  int userId;

    @Column
    private String title;

    @Column
    private int ticketId;

    @Column
    private int status;

    @Column
    private String rease;

    @Column
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRease() {
        return rease;
    }

    public void setRease(String rease) {
        this.rease = rease;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", ticketId=" + ticketId +
                ", status=" + status +
                ", rease='" + rease + '\'' +
                ", amount=" + amount +
                '}';
    }
}

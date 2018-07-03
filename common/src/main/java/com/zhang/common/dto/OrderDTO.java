package com.zhang.common.dto;

import java.io.Serializable;

public class OrderDTO implements Serializable {

    private int id;

    private  int userId;

    private String title;

    private int ticketId;

    private int status;

    private String rease;

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

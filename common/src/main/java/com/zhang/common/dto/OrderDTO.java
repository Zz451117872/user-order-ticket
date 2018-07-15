package com.zhang.common.dto;

import java.io.Serializable;

public class OrderDTO implements Serializable {

    private Integer id;

    private  Integer userId;

    private String title;

    private Integer ticketId;

    private Integer status;

    private String rease;

    private Integer amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getStatus() {
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

    public Integer getAmount() {
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

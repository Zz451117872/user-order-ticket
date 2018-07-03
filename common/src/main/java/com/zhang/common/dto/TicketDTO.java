package com.zhang.common.dto;

import java.io.Serializable;

public class TicketDTO implements Serializable {
    private int id;

    private String name;

    private int owner;

    private int locker;

    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getLocker() {
        return locker;
    }

    public void setLocker(int locker) {
        this.locker = locker;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", locker=" + locker +
                ", amount=" + amount +
                '}';
    }
}

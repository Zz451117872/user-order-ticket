package com.zhang.ticket.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "ticket1")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private int owner;

    @Column
    private int locker;

    @Column
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
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", locker=" + locker +
                ", amount=" + amount +
                '}';
    }
}

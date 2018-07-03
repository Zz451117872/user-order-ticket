package com.zhang.ticket.service;

import com.zhang.ticket.dao.TicketDao;
import com.zhang.ticket.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketDao ticketDao;

    @Transactional
    public void createTicket(Ticket ticket) {
    }

    public List<Ticket> getAll() {
        return ticketDao.findAll();
    }

    @Transactional
    public void deleteOne(Integer id) {
        ticketDao.deleteById( id );
    }

    @Transactional
    public void deleteAll() {
        ticketDao.deleteAllInBatch();
    }

    @Transactional
    public int lockTicket( Integer locker , Integer ticketNo){
        return ticketDao.lockTicket( locker , ticketNo );
    }
}

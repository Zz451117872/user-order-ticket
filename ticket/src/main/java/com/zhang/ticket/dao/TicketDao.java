package com.zhang.ticket.dao;

import com.zhang.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends JpaRepository<Ticket,Integer> {

    @Modifying
    @Query("update ticket1 set locker = ?1 where locker is null and ticketNo = ?2")
    int lockTicket( Integer locker , Integer ticketNo);

    @Modifying
    @Query("update ticket1 set owner = ?1 , locker = null where id = ?2 and locker = ?3 ")
    int moveTicket( Integer owner  , Integer ticketId , Integer locker);

    @Modifying
    @Query("update ticket1 set owner = null , locker = null where id = ?1 and owner = ?2 ")
    int unmoveTicket( Integer ticketId , Integer owner);

    @Modifying
    @Query("update ticket1 set locker = null where id = ?1 and locker = ?2 ")
    int unlockTicket( Integer ticketId , Integer locker);
}

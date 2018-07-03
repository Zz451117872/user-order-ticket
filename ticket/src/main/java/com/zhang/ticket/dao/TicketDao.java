package com.zhang.ticket.dao;

import com.zhang.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends JpaRepository<Ticket,Integer> {

    @Modifying
    @Query("update ticket1 set locker = ? where locker is null and ticketNo = ?")
    int lockTicket( Integer locker , Integer ticketNo);

    @Modifying
    @Query("update ticket1 set owner = ? , locker = null where id = ? and locker = ? ")
    int moveTicket( Integer owner  , Integer ticketId , Integer locker);

    @Modifying
    @Query("update ticket1 set owner = null , locker = null where id = ? and owner = ? ")
    int unmoveTicket( Integer ticketId , Integer owner);

    @Modifying
    @Query("update ticket1 set locker = null where id = ? and locker = ? ")
    int unlockTicket( Integer ticketId , Integer locker);
}

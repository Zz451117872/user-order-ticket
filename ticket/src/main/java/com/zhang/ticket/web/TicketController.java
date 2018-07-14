package com.zhang.ticket.web;

import com.zhang.ticket.domain.Ticket;
import com.zhang.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @RequestMapping("/createTicket")
    @ResponseBody
    public String createTicket( String name , Integer amount ){
        System.out.println( "xxxx");
        Ticket ticket = new Ticket();
        ticket.setName( name );
        ticket.setAmount( amount );
        ticketService.createTicket( ticket );
        return "success";
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public List<Ticket> getAll( ){
        return ticketService.getAll();
    }

    @RequestMapping("/deleteOne")
    @ResponseBody
    public String deleteOne( Integer id ){
        ticketService.deleteOne( id );
        return "seccess";
    }

    @RequestMapping("/deleteAll")
    @ResponseBody
    public String deleteAll( ){
        ticketService.deleteAll();
        return "success";
    }
}

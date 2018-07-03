package com.zhang.user.web;

import com.zhang.user.domain.Customer;
import com.zhang.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/createCustomer")
    @ResponseBody
    public String create( String name ){
        customerService.create( name );
        return "success";
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public List<Customer> getAll(){
        return customerService.getAll();
    }

    @RequestMapping("/deleteOne")
    @ResponseBody
    public String deleteOne( Integer id){
        customerService.deleteOne( id );
        return "success";
    }

    @RequestMapping("/deleteAll")
    @ResponseBody
    public String deleteAll(){
        customerService.deleteAll();
        return "success";
    }
}

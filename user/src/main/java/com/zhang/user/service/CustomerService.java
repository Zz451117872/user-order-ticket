package com.zhang.user.service;

import com.zhang.user.Feign.OrderClient;
import com.zhang.user.dao.CustomerDao;
import com.zhang.user.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrderClient orderClient;

    public void deleteOne(Integer id) {
        customerDao.deleteById( id );
    }

    public void deleteAll() {
        customerDao.deleteAllInBatch();;
    }

    public List<Customer> getAll() {
        return customerDao.findAll();
    }

    public void create(String name) {
        Customer customer = new Customer();
        customer.setAmount( 1000 );
        customer.setName( name );
        customer.setPasword( "123" );
        customerDao.save( customer );
    }
}

package com.zhang.user.dao;

import com.zhang.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends JpaRepository<Customer , Integer> {

    @Modifying
    @Query("update customer1 set amount = amount  - ? where id = ? and amount = ?")
    int  updateAmount(Integer updateAmount ,Integer userid, Integer currentAmount );
}

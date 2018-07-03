package com.zhang.user.dao;

import com.zhang.user.domain.RefundMoneyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundMoneyLogDao extends JpaRepository<RefundMoneyLog , Integer> {
}

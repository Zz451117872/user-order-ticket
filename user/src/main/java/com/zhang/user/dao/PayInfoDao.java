package com.zhang.user.dao;

import com.zhang.user.domain.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayInfoDao extends JpaRepository<PayInfo,Integer>{
}

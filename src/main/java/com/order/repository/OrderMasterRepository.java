package com.order.repository;

import com.order.entity.OrderMaster;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMasterRepository
        extends JpaRepository<OrderMaster, Long> {

    List<OrderMaster> findByCustomerId(String customerId);

    Page<OrderMaster>
    findByCustomerIdOrderByOrderIdDesc(String customerId, Pageable pageable);
}

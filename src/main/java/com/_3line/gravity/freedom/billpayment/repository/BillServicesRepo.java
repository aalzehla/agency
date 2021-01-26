package com._3line.gravity.freedom.billpayment.repository;

import com._3line.gravity.freedom.billpayment.models.BillServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author JoyU
 * @date 10/15/2018
 */
public interface BillServicesRepo extends JpaRepository<BillServices, Long> {
    BillServices findByServiceName(String serviceName);

    BillServices findBillById(Long id);

    List<BillServices> findByCategory(Long category);

    Page<BillServices> findAllBy(Pageable pageable);
}

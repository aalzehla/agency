package com._3line.gravity.freedom.billpayment.repository;

import com._3line.gravity.freedom.billpayment.models.BillCategory;
import com._3line.gravity.freedom.billpayment.models.BillType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JoyU
 * @date 10/15/2018
 */
public interface BillTypeRepo extends JpaRepository<BillType, Long> {

    BillType findByCategoryCode(String categoryCode);
}

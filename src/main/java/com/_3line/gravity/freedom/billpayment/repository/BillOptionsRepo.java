package com._3line.gravity.freedom.billpayment.repository;

import com._3line.gravity.freedom.billpayment.models.BillOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JoyU
 * @date 10/15/2018
 */
public interface BillOptionsRepo extends JpaRepository<BillOption, Long> {
    BillOption findByCode(String code);

}

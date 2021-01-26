package com._3line.gravity.freedom.thirftmgt.repositories;

import com._3line.gravity.freedom.thirftmgt.models.FreedomThrift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FreedomThriftRepository extends JpaRepository<FreedomThrift, Long> {

    FreedomThrift findByCardNumber(String cardNumber);

    FreedomThrift findByUserId(String userId);

    FreedomThrift findByIdNumber(String idNumber);

    FreedomThrift findByCustomerPhone(String phone);

    List<FreedomThrift>findAllByNextliquidationDate(Date nextLiquidationDate);

}

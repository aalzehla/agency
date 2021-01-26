package com._3line.gravity.freedom.bankdetails.repository;

import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Utosu Joy
 */
@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {

    BankDetails findByBankName(String bankName);

    BankDetails findBankDetailsById(Long id);

    BankDetails findByBankCode(String bankCode);

    BankDetails findByCbnCode(String cbnCode);

    List<BankDetails> findAllByIsIntegratedTrue();

    List<BankDetails> findAllByCardRequestFeeIsNotNullAndCardRequestChargeIsNotNull();

    List<BankDetails> findByIsIntegratedIs(boolean isIntegrated);

    Page<BankDetails> findByIsIntegratedIs(boolean isIntegrated, Pageable pageable);

    Page<BankDetails> findAllByIsIntegratedTrue(Pageable pageable);

    Page<BankDetails> findAll(Pageable pageable);

    Page<BankDetails> findByBankNameContains(String bankName,Pageable pageable);






}

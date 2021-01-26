package com._3line.gravity.freedom.bankdetails.service;

import com._3line.gravity.freedom.bankdetails.dtos.BankDetailsDTO;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author Utosu Joy
 */
public interface BankDetailsService {

    //This method is used to find All the Banks Details
    List<BankDetails> findAllBankDetails();

    //This method is used to find All the Banks Details pageable format
    Page<BankDetails> findAllBankDetailsPageable(Pageable pageable);

    Page<BankDetails> findAllBankDetailsDTOPageable(Pageable pageable);

    Page<BankDetails> findAllBankDetailsDTOPageable(String name,Pageable pageable);

    //This method is used to find one Bank Detail
    BankDetails findOne(Long id);

     BankDetails findByCode(String bankCode);

     BankDetails findByCBNCode(String bankCode);

    //This method is used to find All the Integrated Banks Details
    List<BankDetails> findIntegratedBanks(boolean isIntegrated);

    List<BankDetails> findCardEnabledBanks();

    //This method is used to find All the Integrated Banks Details pageable format
    Page<BankDetails> findIntegratedBanks(Pageable pageable);


    BankDetailsDTO convertEntityToDTO(BankDetails bankDetails);

    @PreAuthorize("hasAuthority('MANAGE_FINANCIAL_INSTITUTIONS')")
    String createBank(BankDetailsDTO bankDetailsDTO);

    BankDetailsDTO getBank(Long id);

    @PreAuthorize("hasAuthority('MANAGE_FINANCIAL_INSTITUTIONS')")
    String updateBankDetails(BankDetailsDTO bankDetailsDTO);

}

package com._3line.gravity.freedom.accountmgt.service;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.BankAccountOpeningDTO;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.BankAccountOpeningRequest;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.NameEnquiryDTO;
import com._3line.gravity.freedom.accountmgt.dtos.walletopening.WalletAccountOpeningDTO;
import com._3line.gravity.freedom.accountmgt.dtos.walletopening.WalletAccountOpeningRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {

    Response openBankAccount(BankAccountOpeningRequest bankAccountOpeningRequest);

    Response openWalletAccount(WalletAccountOpeningRequest walletAccountOpeningRequest);

    List<String> getAccountTypes();

    List<String> getBranches() ;

    NameEnquiryDTO doNameEnquiry(String acctNumber);

    Page<BankAccountOpeningDTO> findBankAccountOpenings(String pattern, Pageable pageable);

    Page<BankAccountOpeningDTO> findBankAccountOpeningsByAccountNumberFromAndToDate(String accountNumber, String agentId, String fromDate, String toDate, Pageable pageable);

    Page<BankAccountOpeningDTO> findBankAccountOpeningsByAccNumber(String accountNumber, Pageable pageable);

    Page<WalletAccountOpeningDTO> findWalletAccountOpenings(String pattern, Pageable pageable);

    Page<BankAccountOpeningDTO> getBankAccountOpenings(Pageable pageable);

    Page<WalletAccountOpeningDTO> getWalletAccountOpenings(Pageable pageable);


    Page<WalletAccountOpeningDTO> findWalletAccountOpeningsByPhoneNumberFromAndToDate(String phoneNumber, String from, String to, Pageable pageable);
}

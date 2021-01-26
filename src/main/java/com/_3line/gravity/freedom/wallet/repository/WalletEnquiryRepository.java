package com._3line.gravity.freedom.wallet.repository;

import com._3line.gravity.freedom.wallet.models.CreditRequest;
import com._3line.gravity.freedom.wallet.models.Status;
import com._3line.gravity.freedom.wallet.models.WalletEnquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletEnquiryRepository extends JpaRepository<WalletEnquiry, Long> {

    WalletEnquiry findByEnquiryRequestId(String requestId);


    WalletEnquiry findByEnquiryRequestIdAndHasTransactionIsNot(String requestId,boolean hasTransaction);
}

package com._3line.gravity.freedom.accountmgt.repository;


import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.accountmgt.model.WalletAccountOpening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface WalletAccountOpeningRepository extends AppCommonRepository<WalletAccountOpening, Long> {
    WalletAccountOpening findByMobilePhone(String mobilePhone);

    Page<WalletAccountOpening> findByCreatedOnBetweenOrderByCreatedOnDesc(Date startDate, Date endDate, Pageable pageable);

    Page<WalletAccountOpening> findAllByMobilePhone(String phoneNumber, Pageable pageable);

    Page<WalletAccountOpening> findByMobilePhoneAndCreatedOnBetweenOrderByCreatedOnDesc(String phoneNumber, Date startDate, Date endDate, Pageable pageable);

    Page<WalletAccountOpening> findAllByOrderByCreatedOnDesc(Pageable pageable);
}

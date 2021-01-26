package com._3line.gravity.freedom.wallet.repository;

import com._3line.gravity.freedom.wallet.models.FreedomWallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreedomWalletRepository extends JpaRepository<FreedomWallet, Long> {

    FreedomWallet findByWalletNumber(String walletNumber);

    List<FreedomWallet> findByWalletNumberLike(String wallerNumber);

    Page <FreedomWallet> findByIsGeneralLedger(String isGeneralLedger , Pageable pageable);



}

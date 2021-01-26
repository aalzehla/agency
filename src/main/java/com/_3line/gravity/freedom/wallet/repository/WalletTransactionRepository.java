package com._3line.gravity.freedom.wallet.repository;

import com._3line.gravity.freedom.wallet.models.FreedomWallet;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com._3line.gravity.freedom.wallet.models.TranType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<FreedomWalletTransaction, Long> {

    List<FreedomWalletTransaction> findByTranID(String tranID);

    List<FreedomWalletTransaction>findByWalletNumber(String walletNum);

    Page<FreedomWalletTransaction>findAllByWalletNumberOrderByTranDateDesc(String walletNumber,Pageable pageable);

    Page<FreedomWalletTransaction>findAllByWalletNumberAndTranDateBetweenOrderByTranDateDesc(String walletNumber,Date from, Date to,Pageable pageable);


    List<FreedomWalletTransaction>findByWalletNumberAndTranDateBetweenOrderByTranDateDesc(String walletNum,
                                                                                    Date from,
                                                                                    Date to);

    List<FreedomWalletTransaction>findByWalletNumberAndTranDateBetweenOrderByIdDesc(String walletNum,
                                                                              Date from,
                                                                              Date to);

    List<FreedomWalletTransaction>findByWalletNumberAndTranTypeAndTranDateBetweenOrderByIdDesc(String walletNum,
                                                                                         String tranType,
                                                                                         Date from,
                                                                                         Date to);

    Page<FreedomWalletTransaction>findByWalletNumberLikeOrTranIDEqualsOrRemarkContainingAndTranDateBetweenOrderByTranDateDesc(String walletNumber , String tranId , String remark, Date from , Date to , Pageable pageable);

    List<FreedomWalletTransaction> findByTranDateBetweenAndWalletNumberOrderByTranDate(Date from,
                                                                                       Date to,
                                                                                       String wallet_num);

    List<FreedomWalletTransaction>findByWalletNumberAndTranDateLessThanOrderByTranDateDesc(String wallet_num,
                                                                                           Date tranDate);


}

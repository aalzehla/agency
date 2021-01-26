package com._3line.gravity.freedom.wallet.dto;

import com._3line.gravity.freedom.transactions.dtos.TransactionHistoryRespDTO;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WalletTransactionListDto {
    private boolean hasNextRecord;
    private int totalCount;

    @JsonProperty("transactions")
    private List<FreedomWalletTransaction> walletTransactions = new ArrayList<>();

}

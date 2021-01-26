package com._3line.gravity.freedom.transactions.dtos;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TransactionListDto{
    private boolean hasNextRecord;
    private int totalCount;

    @JsonProperty("transactions")
    private List<TransactionHistoryRespDTO> transactions = new ArrayList<>();

}

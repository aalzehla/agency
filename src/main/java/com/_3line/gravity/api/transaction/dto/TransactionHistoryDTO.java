package com._3line.gravity.api.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class TransactionHistoryDTO {

    @JsonIgnore
    private String agentName ;

    private String fromDate;
    private String toDate;
    private int size;
    private int page;
}

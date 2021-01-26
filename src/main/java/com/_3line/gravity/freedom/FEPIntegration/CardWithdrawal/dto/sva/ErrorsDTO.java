package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva;

import lombok.Data;

import java.util.List;

@Data
public class ErrorsDTO {

    public List<ErrorDTO> errors = null;
    public ErrorDTO error;
}

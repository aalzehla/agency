package com._3line.gravity.freedom.agents.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import lombok.Data;

@Data
public class PinUpdateDTO extends AbstractVerifiableDto {

    private String agentName;
    private String oldPin;
    private String newPin;

}

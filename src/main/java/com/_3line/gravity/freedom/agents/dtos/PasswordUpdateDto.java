package com._3line.gravity.freedom.agents.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import lombok.Data;

@Data
public class PasswordUpdateDto {

    private String agentUsername;
    private String oldPassword;
    private String newPassword;


}

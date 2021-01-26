package com._3line.gravity.core.otp.model;


import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author FortunatusE
 * @date 3/21/2019
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Data
public class OtpRequest extends AbstractEntity {

    public OtpRequest() {
    }

    public OtpRequest(String otp, String agentId) {
        this.otp = otp;
        this.agentId = agentId;
    }

    private String otp;
    private String agentId;
    private LocalDateTime generatedOn;
    private LocalDateTime expiredOn;
    private boolean verified;
}

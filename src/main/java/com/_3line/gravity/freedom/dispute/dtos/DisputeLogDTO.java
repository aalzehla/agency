package com._3line.gravity.freedom.dispute.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DisputeLogDTO extends AbstractVerifiableDto {

    private Long tranId;
    private Double tranAmount;
    private String tranType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tranDate;
    private String complaint;
    private String image;

    private String rrn;
    private String terminalId;
    private String channel;
    private String pan;
    private String remark;

    private String loggedBy;
    private String raisedBy;

}

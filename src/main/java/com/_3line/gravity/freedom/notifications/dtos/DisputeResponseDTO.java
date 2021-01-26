package com._3line.gravity.freedom.notifications.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class DisputeResponseDTO {

    Long tranId;
    Double tranAmount;
    String tranType;
    String complaint;
    Date tranDate;
    String status;
    Date treatedOn;
    String treatedBy;

}

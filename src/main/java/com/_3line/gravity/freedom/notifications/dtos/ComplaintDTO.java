package com._3line.gravity.freedom.notifications.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ComplaintDTO {

    String complaint;
    String treatedBy;
    Date treatedOn;
    String status;

}

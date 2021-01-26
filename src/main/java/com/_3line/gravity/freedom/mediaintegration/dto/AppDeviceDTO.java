package com._3line.gravity.freedom.mediaintegration.dto;

import lombok.Data;

@Data
public class AppDeviceDTO {

    private String id;
    private String deviceType;
    private String appVersion;
    private String createdOn;
    private String updatedOn;
    private String updateBy;
}

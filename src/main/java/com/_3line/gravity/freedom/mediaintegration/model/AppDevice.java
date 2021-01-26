package com._3line.gravity.freedom.mediaintegration.model;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class AppDevice extends AbstractEntity {

    private String deviceType;
    private String appVersion;
    private String updateBy;
    private Date updatedOn;

}

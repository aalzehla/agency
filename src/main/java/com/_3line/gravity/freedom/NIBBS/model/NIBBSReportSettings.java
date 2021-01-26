package com._3line.gravity.freedom.NIBBS.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.core.usermgt.model.SystemUser;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class NIBBSReportSettings extends AbstractEntity {

    private Boolean automateReportSending;
    @OneToOne
    private SystemUser updatedBy;
    private Date updatedOn;

    @DateTimeFormat(pattern="YYYY-MM-dd'T'HH:mm")
    private Date nextRunDate;
    @DateTimeFormat(pattern="YYYY-MM-dd'T'HH:mm")
    private Date lastRunDate;
    /**
     * next fetch date range in minutes
     */
    private int fetchInterval;
}

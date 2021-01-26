package com._3line.gravity.core.verification.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Where(clause ="del_Flag='N'" )
public class Verification extends AbstractEntity {


    @Lob
    private String beforeObject;
    @Lob
    private String afterObject;
    @Lob
    private String originalObject;
    @Enumerated(value = EnumType.STRING)
    private VerificationStatus status;
    private String description;
    private Long entityId;
    private String entityName;
    private String operation;
    private String comments;
    private String initiatedBy;
    private Date initiatedOn;
    private String verifiedBy;
    private Date verifiedOn;
    private String entityPackage;
    private String methodName ;
    private String className ;
    private String code ;
//    @Lob
    @Column
    private String reason;
    private String ipInitiatedFrom;



}

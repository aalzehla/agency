package com._3line.gravity.core.verification.dtos;

import com._3line.gravity.core.verification.models.VerificationStatus;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.util.Date;

public class VerificationDto {


    private Long id;
    private int version;
    private String beforeObject; //json
    private String afterObject; //json
    private String original; //json

    private VerificationStatus status;

    private String description;
    private Long entityId;
    private String entityName;
    private String operation;

    private String initiatedBy;

    private Date initiatedOn;

    @NotEmpty(message = "comments")
    private String comments;

    private String verifiedBy;
    private Date verifiedOn;
    private String entityPackage;
    private String methodName ;
    private String className ;
    private String code ;

    private String ipInitiatedFrom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getBeforeObject() {
        return beforeObject;
    }

    public void setBeforeObject(String beforeObject) {
        this.beforeObject = beforeObject;
    }

    public String getAfterObject() {
        return afterObject;
    }

    public void setAfterObject(String afterObject) {
        this.afterObject = afterObject;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public Date getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(Date initiatedOn) {
        this.initiatedOn = initiatedOn;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Date getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(Date verifiedOn) {
        this.verifiedOn = verifiedOn;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIpInitiatedFrom() {
        return ipInitiatedFrom;
    }

    public void setIpInitiatedFrom(String ipInitiatedFrom) {
        this.ipInitiatedFrom = ipInitiatedFrom;
    }
}

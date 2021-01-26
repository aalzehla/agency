package com._3line.gravity.freedom.service.tranupdate.dto;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;

public class TransactionUpdateDto extends AbstractVerifiableDto {

    private String initiatedBy;
    private Integer noOfTransactions;
    private Integer noOfSuccessFull;
    private Integer noOfFailed;
    private Integer noOfReversals;
    private String approvedBy;
    private Integer totalVolume;
    private Double totalSuccessFullVolume;
    private Double totalFailedVolume;
    private Long fileId;


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public Integer getNoOfTransactions() {
        return noOfTransactions;
    }

    public void setNoOfTransactions(Integer noOfTransactions) {
        this.noOfTransactions = noOfTransactions;
    }

    public Integer getNoOfSuccessFull() {
        return noOfSuccessFull;
    }

    public void setNoOfSuccessFull(Integer noOfSuccessFull) {
        this.noOfSuccessFull = noOfSuccessFull;
    }

    public Integer getNoOfFailed() {
        return noOfFailed;
    }

    public void setNoOfFailed(Integer noOfFailed) {
        this.noOfFailed = noOfFailed;
    }

    public Integer getNoOfReversals() {
        return noOfReversals;
    }

    public void setNoOfReversals(Integer noOfReversals) {
        this.noOfReversals = noOfReversals;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Integer getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Integer totalVolume) {
        this.totalVolume = totalVolume;
    }


    public Double getTotalSuccessFullVolume() {
        return totalSuccessFullVolume;
    }

    public void setTotalSuccessFullVolume(Double totalSuccessFullVolume) {
        this.totalSuccessFullVolume = totalSuccessFullVolume;
    }

    public Double getTotalFailedVolume() {
        return totalFailedVolume;
    }

    public void setTotalFailedVolume(Double totalFailedVolume) {
        this.totalFailedVolume = totalFailedVolume;
    }

    @Override
    public String toString() {
        return "TransactionUpdateDto{" +
                "initiatedBy='" + initiatedBy + '\'' +
                ", noOfTransactions=" + noOfTransactions +
                ", noOfSuccessFull=" + noOfSuccessFull +
                ", noOfFailed=" + noOfFailed +
                ", noOfReversals=" + noOfReversals +
                ", approvedBy='" + approvedBy + '\'' +
                ", totalVolume=" + totalVolume +
                ", totalSuccessFullVolume=" + totalSuccessFullVolume +
                ", totalFailedVolume=" + totalFailedVolume +
                ", fileId=" + fileId +
                '}';
    }
}


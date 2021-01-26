package com._3line.gravity.freedom.billpayment.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;

/**
 * @author JoyU
 * @date 10/15/2018
 */
@Entity
public class BillAudit extends AbstractEntity {
    private String name;
    private String responseCode;
    private String responseDescription;
    private String code;
    private String amount;
    private Boolean isFixedAmount;
    private String requestRef;
    private String paymentCode;
    private String customerId;
    private String signature;
    private String transactionRef;
    private String pin;
    private String fullname;
    private String mobilePhone;
    private String email;
}

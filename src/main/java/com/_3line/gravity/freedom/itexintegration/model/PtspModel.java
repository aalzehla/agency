package com._3line.gravity.freedom.itexintegration.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"terminal_id","rrn","reversal"})})
public class PtspModel implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    Long id;

    @Version
    protected int version;

    protected String delFlag = "N";

    protected Date deletedOn;

    protected final Date createdOn = new Date();

    @Column(name = "terminal_id")
    private String terminalId;
    private Double amount;
    private String transactionTime;
    private String ptspTranDate;
    private String stan;
    private String rrn;
    private String cardNumber;
    private String productId;
    private String statusCode;
    private String pan;
    private String reversal;
    private String bank;
    private String transactionType;
    private String ptsp ;
    private String verifiedBy ;
    private Boolean isVerified = false;
    private Boolean processed = false;

    private String uploadedBy ;
    private String processor_status ;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PtspModel ptspModel = (PtspModel) o;
        return terminalId.equals(ptspModel.terminalId) &&
                rrn.equals(ptspModel.rrn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode() + 1 * 13, terminalId, rrn);
    }
}

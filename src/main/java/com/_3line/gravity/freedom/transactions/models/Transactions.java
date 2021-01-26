package com._3line.gravity.freedom.transactions.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author NiyiO
 */
@Entity
@Table(name = "transactions")
@Data
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tran_id")
    private Long tranId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "tran_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tranDate;

    @Size(max = 30)
    @Column(name = "masked_pan")
    private String maskedPan;

    @Size(max = 30)
    @Column(name = "media")
    private String media;

    @Size(max = 90)
    @Column(name = "description")
    private String description;

    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private short status;

    @Size(max = 20)
    @Column(name = "statusdescription")
    private String statusdescription;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;

    @Size(max = 19)
    @Column(name = "transaction_type")
    private String transactionType;

    @Size(max = 30)
    @Column(name = "biller_type")
    private String billerType;

    @Column(name = "walletid")
    private BigInteger walletid;

    @Column(name = "transaction_type_description")
    private String transactionTypeDescription;

    @Basic(optional = false)
    @NotNull
    @Column(name = "innitiator_id")
    private long innitiatorId;

    @Size(max = 100)
    @Column(name = "webpay_tran_reference")
    private String webpayTranReference;

    @Size(max = 40)
    @Column(name = "stan")
    private String stan;

    @Size(max = 20)
    @Column(name = "terminal_id")
    private String terminalId;

    @Size(max = 70)
    @Column(name = "merchant_loc")
    private String merchantLoc;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Size(max = 70)
    @Column(name = "hashedpan")
    private String hashedpan;

    @Column(name = "productid")
    private BigInteger productid;

    @Column(name = "balancebefore")
    private Double balancebefore;

    @Basic(optional = false)
    @NotNull
    @Column(name = "approval")
    private long approval;

    @Size(max = 20)
    @Column(name = "latitude")
    private String latitude;

    @Size(max = 20)
    @Column(name = "longitude")
    private String longitude;

    private String bankFrom ;

    private String bankTo ;

    private String accountNumber ;

    private String accountNumberTo;

    private String agentName ;

    private String customerName;

    private String fee="0.00";

    private String depositorName;

    private String depositorEmail;

    private String depositorPhone ;

    private String itexTranId ;

    private String statcode ;

    private String transactionReference ;

    @Column(name = "tran_value_date")
    private Date tranValueDate;


    public List<String> getDefaultSearchFields() {
        return Arrays.asList("terminalId"  ,"accountNumber" , "accountNumberTo" , "agentName" , "stan" , "depositorName" , "bankTo" , "itexTranId") ;
    }


    public interface CustomTranEntity {

        String getAgentName();
        String getSum();
    }


    public Transactions() {
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.tranId);
        hash = 83 * hash + Objects.hashCode(this.tranDate);
        hash = 83 * hash + Objects.hashCode(this.maskedPan);
        hash = 83 * hash + Objects.hashCode(this.media);
        hash = 83 * hash + Objects.hashCode(this.description);
        hash = 83 * hash + this.status;
        hash = 83 * hash + Objects.hashCode(this.statusdescription);
        hash = 83 * hash + Objects.hashCode(this.amount);
        hash = 83 * hash + Objects.hashCode(this.transactionType);
        hash = 83 * hash + Objects.hashCode(this.billerType);
        hash = 83 * hash + Objects.hashCode(this.walletid);
        hash = 83 * hash + Objects.hashCode(this.transactionTypeDescription);
        hash = 83 * hash + (int) (this.innitiatorId ^ (this.innitiatorId >>> 32));
        hash = 83 * hash + Objects.hashCode(this.webpayTranReference);
        hash = 83 * hash + Objects.hashCode(this.stan);
        hash = 83 * hash + Objects.hashCode(this.terminalId);
        hash = 83 * hash + Objects.hashCode(this.merchantLoc);
        hash = 83 * hash + Objects.hashCode(this.beneficiary);
        hash = 83 * hash + Objects.hashCode(this.hashedpan);
        hash = 83 * hash + Objects.hashCode(this.productid);
        hash = 83 * hash + Objects.hashCode(this.balancebefore);
        hash = 83 * hash + (int) (this.approval ^ (this.approval >>> 32));
        hash = 83 * hash + Objects.hashCode(this.latitude);
        hash = 83 * hash + Objects.hashCode(this.longitude);
        hash = 83 * hash + Objects.hashCode(this.tranValueDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transactions other = (Transactions) obj;
        if (this.status != other.status) {
            return false;
        }
        if (this.innitiatorId != other.innitiatorId) {
            return false;
        }
        if (this.approval != other.approval) {
            return false;
        }
        if (!Objects.equals(this.maskedPan, other.maskedPan)) {
            return false;
        }
        if (!Objects.equals(this.media, other.media)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.statusdescription, other.statusdescription)) {
            return false;
        }
        if (!Objects.equals(this.transactionType, other.transactionType)) {
            return false;
        }
        if (!Objects.equals(this.billerType, other.billerType)) {
            return false;
        }
        if (!Objects.equals(this.transactionTypeDescription, other.transactionTypeDescription)) {
            return false;
        }
        if (!Objects.equals(this.webpayTranReference, other.webpayTranReference)) {
            return false;
        }
        if (!Objects.equals(this.stan, other.stan)) {
            return false;
        }
        if (!Objects.equals(this.terminalId, other.terminalId)) {
            return false;
        }
        if (!Objects.equals(this.merchantLoc, other.merchantLoc)) {
            return false;
        }
        if (!Objects.equals(this.hashedpan, other.hashedpan)) {
            return false;
        }
        if (!Objects.equals(this.latitude, other.latitude)) {
            return false;
        }
        if (!Objects.equals(this.longitude, other.longitude)) {
            return false;
        }
        if (!Objects.equals(this.tranId, other.tranId)) {
            return false;
        }
        if (!Objects.equals(this.tranDate, other.tranDate)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.walletid, other.walletid)) {
            return false;
        }
        if (!Objects.equals(this.beneficiary, other.beneficiary)) {
            return false;
        }
        if (!Objects.equals(this.productid, other.productid)) {
            return false;
        }
        if (!Objects.equals(this.balancebefore, other.balancebefore)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transactions{" + "tranId=" + tranId + ", tranDate=" + tranDate + ", maskedPan=" + maskedPan + ", media=" + media + ", description=" + description + ", status=" + status + ", statusdescription=" + statusdescription + ", amount=" + amount + ", transactionType=" + transactionType + ", billerType=" + billerType + ", walletid=" + walletid + ", transactionTypeDescription=" + transactionTypeDescription + ", innitiatorId=" + innitiatorId + ", webpayTranReference=" + webpayTranReference + ", stan=" + stan + ", terminalId=" + terminalId + ", merchantLoc=" + merchantLoc + ", beneficiary=" + beneficiary + ", hashedpan=" + hashedpan + ", productid=" + productid + ", balancebefore=" + balancebefore + ", approval=" + approval + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }

}

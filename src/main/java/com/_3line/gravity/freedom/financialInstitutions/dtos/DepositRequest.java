package com._3line.gravity.freedom.financialInstitutions.dtos;


import com._3line.gravity.api.transaction.dto.CardRequestDTO;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@ToString
public class DepositRequest {

    public DepositRequest() {

    }


    public DepositRequest(CardRequestDTO  cardRequestDTO, Agents agent) {

        this.agentPin = cardRequestDTO.getAgentPin();
        this.bankName = String.valueOf(cardRequestDTO.getBankName());
        this.bankCode = cardRequestDTO.getBankName();
        this.customerName=agent.getFirstName() + " "+agent.getLastName();
        this.depositor = agent.getUsername();
        this.customerEmail = agent.getEmail();
        this.customerPhone = agent.getPhoneNumber();
        this.longitude  = cardRequestDTO.getLongitude();
        this.latitude = cardRequestDTO.getLatitude();
        this.deviceId = cardRequestDTO.getDeviceId();
        this.narration = cardRequestDTO.getNarration()==null? "":cardRequestDTO.getNarration();
        this.media = "MOBILE";
        this.amount = "0.00";


    }



    public String accountNumber ;
    public String bankCode;
    public String amount;
    public String customerName;
    public String depositor;
    public String customerEmail;
    public String customerPhone;
    public String bankName;
    public String customerPin;
    public String agentPin;
    public String longitude ;
    public String latitude;
    public String deviceId;
    public String totalAmount ;
    public String narration;
    public String media;
//    public String token ;



}

package com._3line.gravity.freedom.device.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.wallet.models.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Data
public class DeviceOperator extends AbstractEntity implements PrettySerializer {


    @Column(unique = true)
    private String operatorId;

    private String name;
    private String password;
    private String pin;
    private String phoneNumber;
    private String emailAddress;

    private boolean enabled = true;

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    @ManyToOne
    private Agents parent ;

    @ManyToOne
    private DeviceAudit deviceAudit;


    @Override
    public String toString() {
        return "DeviceOperator{" +
                "operatorId='" + operatorId + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", pin='" + pin + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", enabled=" + enabled +
                ", wallet=" + wallet +
                '}';
    }




    @Override
    @JsonIgnore
    public JsonSerializer<DeviceOperator> getSerializer() {
        return new JsonSerializer<DeviceOperator>() {
            @Override
            public void serialize(DeviceOperator operator, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

                gen.writeStartObject();
                gen.writeStringField("Operator ID", operatorId);
                gen.writeStringField("Name", name);
                gen.writeStringField("Phone Number", phoneNumber);
                gen.writeStringField("Parent Agent ID", parent.getAgentId());
                gen.writeStringField("Status", enabled?"Enabled":"Disabled");
                gen.writeEndObject();
            }
        };
    }

}

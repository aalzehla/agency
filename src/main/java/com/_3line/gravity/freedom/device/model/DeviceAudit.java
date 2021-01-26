package com._3line.gravity.freedom.device.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com._3line.gravity.freedom.agents.models.Agents;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
public class DeviceAudit extends AbstractEntity implements PrettySerializer {


    @Enumerated(EnumType.STRING)
    private DeviceType type;
//    @Column(unique = true, nullable = false)
    private String terminalId;
    private byte[] deviceKey;
    private String deviceId;
    private String sessionKey;
    private String agentIdentifier;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status = DeviceStatus.ENABLED;

    @ManyToMany
    private List<Agents> agents = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<DeviceOperator> operators = new ArrayList<>();

    private String comment;


    public List<DeviceOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<DeviceOperator> operators) {
        this.operators = operators;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public byte[] getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(byte[] deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public List<Agents> getAgents() {
        return agents;
    }

    public void setAgents(List<Agents> agents) {
        this.agents = agents;
    }

    public SecretKey getKey() throws Exception {
        SecretKey originalKey = new SecretKeySpec(this.deviceKey, 0, this.deviceKey.length, "AES");

        return  originalKey ;
    }

    public String getAgentIdentifier() {
        return agentIdentifier;
    }

    public void setAgentIdentifier(String agentIdentifier) {
        this.agentIdentifier = agentIdentifier;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceAudit)) return false;
        if (!super.equals(o)) return false;

        DeviceAudit deviceAudit = (DeviceAudit) o;

        return deviceId.equals(deviceAudit.deviceId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + deviceId.hashCode();
        return result;
    }

    @Override
    @JsonIgnore
    public JsonSerializer<DeviceAudit> getSerializer() {
        return new JsonSerializer<DeviceAudit>() {
            @Override
            public void serialize(DeviceAudit deviceAudit, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

                gen.writeStartObject();
                gen.writeStringField("DeviceAudit ID", deviceId!=null?deviceId:"");
                gen.writeStringField("Terminal ID", terminalId);
                gen.writeStringField("Status", DeviceStatus.ENABLED.equals(status)?"Enabled":"Disabled");
                gen.writeStringField("Comment", comment!=null?comment:"");
                gen.writeEndObject();
            }
        };
    }



    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("terminalId", "deviceId", "agentIdentifier");
    }

    @Override
    public String toString() {
        return "DeviceAudit{" +
                "type=" + type +
                ", terminalId='" + terminalId + '\'' +
                ", deviceKey=" + Arrays.toString(deviceKey) +
                ", deviceId='" + deviceId + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", status=" + status +
                ", operators=" + operators +
                ", comment='" + comment + '\'' +
                '}';
    }
}

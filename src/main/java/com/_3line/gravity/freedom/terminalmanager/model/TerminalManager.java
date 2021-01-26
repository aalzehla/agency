package com._3line.gravity.freedom.terminalmanager.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.device.model.DeviceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TerminalManager extends AbstractEntity {
    private String terminalId;
    private String status;
    private Boolean isAssigned;
    @OneToOne
    private Agents agents;

}

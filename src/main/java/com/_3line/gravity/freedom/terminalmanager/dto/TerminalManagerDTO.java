package com._3line.gravity.freedom.terminalmanager.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Objects;

@Data
public class TerminalManagerDTO {
    private Long id;
    @JsonIgnoreProperties
    private int version;
    private String terminalId;
    private String status;
    private Boolean isAssigned;
    private String agentDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalManagerDTO that = (TerminalManagerDTO) o;
        return Objects.equals(terminalId, that.terminalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminalId);
    }
}

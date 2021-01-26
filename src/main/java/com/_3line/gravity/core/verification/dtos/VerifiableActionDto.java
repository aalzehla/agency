package com._3line.gravity.core.verification.dtos;


import com.google.common.base.Objects;
import lombok.ToString;

@ToString
public class VerifiableActionDto extends AbstractVerifiableDto {

    private boolean enabled;

    private String code ;

    private String description ;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerifiableActionDto)) return false;
        VerifiableActionDto that = (VerifiableActionDto) o;
        return Objects.equal(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}

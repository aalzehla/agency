package com._3line.gravity.core.verification.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;


@ToString
@Entity
public class VerifiableAction extends AbstractEntity {

    private boolean enabled = true ;

    @Column(unique = true)
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
}

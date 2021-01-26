package com._3line.gravity.core.operation.model;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class Operation extends AbstractEntity {

    private String code ;
    private String description;
    private boolean enabled;
    private boolean makerchecker;


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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isMakerchecker() {
        return makerchecker;
    }

    public void setMakerchecker(boolean makerchecker) {
        this.makerchecker = makerchecker;
    }
}

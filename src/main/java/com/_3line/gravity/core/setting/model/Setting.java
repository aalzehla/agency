package com._3line.gravity.core.setting.model;


import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by FortunatusE on 8/3/2018.
 */


@Entity
@Where(clause ="del_Flag='N'" )
public class Setting extends AbstractEntity {

    private String name;
    private String code;
    private String description;
    private String value;
    private boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("name", "code");
    }

    @Override
    public String toString() {
        return "Setting{" +
                "Id='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

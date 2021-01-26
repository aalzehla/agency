package com._3line.gravity.core.setting.dto;


import javax.validation.constraints.NotEmpty;

/**
 * Created by FortunatusE on 8/3/2018.
 */
public class SettingDTO {

    private Long id;
    private int version;
    @NotEmpty(message = "Name is required")
    private String name;
    private String description;
    @NotEmpty(message = "Code is required")
    private String code;
    private String value;
    private boolean enabled;
    private String csrf;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCsrf() {
        return csrf;
    }

    public void setCsrf(String csrf) {
        this.csrf = csrf;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SettingDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

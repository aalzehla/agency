package com._3line.gravity.core.thirdparty.dto;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;

public class ThirdPartyDto extends AbstractVerifiableDto {

    private String clientName ;

    private String appId ;

    private String key ;

    private boolean enabled ;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

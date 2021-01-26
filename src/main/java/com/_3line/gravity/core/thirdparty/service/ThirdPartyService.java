package com._3line.gravity.core.thirdparty.service;

import com._3line.gravity.core.thirdparty.dto.ThirdPartyDto;
import com._3line.gravity.core.thirdparty.exceptions.ThirdPartyException;

import java.util.List;

public interface ThirdPartyService {

    String createThirdParty(ThirdPartyDto thirdPartyDto) throws ThirdPartyException;

    String enableThirdParty(Long partyId);

    String disableThirdParty(Long partyId);

    void checkAppId(String appId) throws ThirdPartyException;

    ThirdPartyDto getThridParty(String appId);

    String decryptDataForParty(String appId , String data)throws Exception;

    String encryptDataForParty(String appId , String data)throws Exception;

    ThirdPartyDto getThridParty(Long Id);

    List<ThirdPartyDto> getAll();
}

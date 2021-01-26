package com._3line.gravity.core.thirdparty.service.impl;

import com._3line.gravity.core.cryptography.GenerateKeys;
import com._3line.gravity.core.thirdparty.dto.ThirdPartyDto;
import com._3line.gravity.core.thirdparty.exceptions.ThirdPartyException;
import com._3line.gravity.core.thirdparty.model.ThirdPartySystem;
import com._3line.gravity.core.thirdparty.repository.ThirdPartySystemRepository;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.core.utils.AppUtility;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService , InitializingBean {

    @Autowired
    private ThirdPartySystemRepository thirdPartySystemRepository;
    @Autowired
    private ModelMapper modelMapper ;
    private Cipher cipher;
    @Autowired
    private MessageSource messageSource;
    private Locale locale = LocaleContextHolder.getLocale();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        this.cipher = Cipher.getInstance("AES");
    }

    @Override
    public String createThirdParty(ThirdPartyDto thirdPartyDto) throws ThirdPartyException {
        if(thirdPartySystemRepository.existsByClientName(thirdPartyDto.getClientName())){
            throw new ThirdPartyException(messageSource.getMessage("thirdparty.client.exist", null, locale));
        }

        try {
            ThirdPartySystem system = convertDtoToEntity(thirdPartyDto);
            system.setAppId(AppUtility.randomString(10));
            GenerateKeys generateKeys = new GenerateKeys(1024);
            generateKeys.createKeys();
            system.setAppKey(generateKeys.getKey().getEncoded());
            thirdPartySystemRepository.save(system);
            return messageSource.getMessage("thirdparty.client.saved", null, locale) ;
        }catch (Exception e){
            logger.info("error {}",e.getMessage());
            throw new ThirdPartyException(messageSource.getMessage("thirdparty.client.error", null, locale),e);
        }

    }

    @Override
    public String enableThirdParty(Long partyId) {
        ThirdPartySystem system = thirdPartySystemRepository.getOne(partyId);
        if(Objects.isNull(system)){
            throw new ThirdPartyException(messageSource.getMessage("thirdparty.appId.noexist", null, locale));
        }
        system.setEnabled(true);
        thirdPartySystemRepository.save(system);
        return messageSource.getMessage("thirdparty.client.enabled", null, locale);
    }

    @Override
    public String disableThirdParty(Long partyId) {
        ThirdPartySystem system = thirdPartySystemRepository.getOne(partyId);
        if(Objects.isNull(system)){
            throw new ThirdPartyException(messageSource.getMessage("thirdparty.appId.noexist", null, locale));
        }
        system.setEnabled(false);
        thirdPartySystemRepository.save(system);
        return messageSource.getMessage("thirdparty.client.disabled", null, locale);
    }

    @Override
    public void checkAppId(String appId) throws ThirdPartyException {
        ThirdPartySystem system = thirdPartySystemRepository.findByAppId(appId);
        if(Objects.isNull(system)){
            throw new ThirdPartyException(messageSource.getMessage("thirdparty.appId.noexist", null, locale));
        }

        if(!system.isEnabled()){
            throw new ThirdPartyException(messageSource.getMessage("thirdparty.client.disabled", null, locale));
        }
    }

    @Override
    public ThirdPartyDto getThridParty(String appId) {
        return convertEntityToDto(thirdPartySystemRepository.findByAppId(appId));
    }

    @Override
    public String decryptDataForParty(String appId, String data) throws Exception {
        ThirdPartySystem system = thirdPartySystemRepository.findByAppId(appId);
        this.cipher.init(Cipher.DECRYPT_MODE, system.getKey());
        return new String(cipher.doFinal(Base64.decodeBase64(data)), "UTF-8");
    }

    @Override
    public String encryptDataForParty(String appId, String data) throws Exception {
        ThirdPartySystem system = thirdPartySystemRepository.findByAppId(appId);
        this.cipher.init(Cipher.ENCRYPT_MODE, system.getKey());
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes("UTF-8")));
    }

    @Override
    public ThirdPartyDto getThridParty(Long Id) {
        return convertEntityToDto(thirdPartySystemRepository.getOne(Id));
    }

    @Override
    public List<ThirdPartyDto> getAll() {
        return thirdPartySystemRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private ThirdPartySystem convertDtoToEntity(ThirdPartyDto thirdPartyDto){
        return modelMapper.map(thirdPartyDto , ThirdPartySystem.class);
    }

    private ThirdPartyDto convertEntityToDto(ThirdPartySystem thirdPartySystem){
       ThirdPartyDto dto = modelMapper.map(thirdPartySystem , ThirdPartyDto.class);
       dto.setKey(Hex.encodeHexString(thirdPartySystem.getAppKey()));
       return dto ;
    }
}

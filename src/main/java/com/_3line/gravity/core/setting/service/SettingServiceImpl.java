package com._3line.gravity.core.setting.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.model.Setting;
import com._3line.gravity.core.setting.repository.SettingRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Created by FortunatusE on 8/3/2018.
 */

@Service
public class SettingServiceImpl implements SettingService {

    private static final Logger logger = LoggerFactory.getLogger(SettingServiceImpl.class);

    private SettingRepository settingRepository;
    private MessageSource messageSource;
    private ModelMapper modelMapper;
    private final Locale locale = LocaleContextHolder.getLocale() ;


    @Autowired
    public SettingServiceImpl(SettingRepository settingRepository, MessageSource messageSource, ModelMapper modelMapper) {
        this.settingRepository = settingRepository;
        this.messageSource = messageSource;
        this.modelMapper = modelMapper;
    }

    @Override
    public String addSetting(SettingDTO settingDTO) {

        logger.debug("Adding setting {}", settingDTO);

        validateNonExistingSetting(settingDTO);
        Setting setting = convertDtoToEntity(settingDTO);

        try {
            Setting newSetting = settingRepository.save(setting);
            logger.info("Added setting {}", newSetting);
            return messageSource.getMessage("setting.add.success", null, locale);
        }
        catch (Exception e){
            logger.error("Failed to add setting {}", settingDTO, e);
            throw new GravityException(messageSource.getMessage("setting.add.failure", null, locale),e);
        }

    }

    @Override
    public String updateSetting(SettingDTO settingDTO){

        logger.debug("Updating setting {}",settingDTO);

        validateNonExistingSetting(settingDTO);
        Setting setting = convertDtoToEntity(settingDTO);

        try {
            Setting updatedSetting = settingRepository.save(setting);
            logger.info("Updated setting {}", updatedSetting);
            return messageSource.getMessage("setting.update.success", null, locale);
        }
        catch (Exception e){
            logger.error("Failed to update setting {}", settingDTO, e);
            throw new GravityException(messageSource.getMessage("setting.update.failure", null, locale) ,e);
        }
    }


    private void validateNonExistingSetting(SettingDTO settingDTO){

        Setting setting = settingRepository.findByCode(settingDTO.getCode());

        if(setting!=null && !setting.getId().equals(settingDTO.getId())){
            throw new GravityException(messageSource.getMessage("setting.exists", null, locale));
        }
    }

    @Override
    public SettingDTO getSettingById(Long id) {

        logger.debug("Getting setting by Id [{}]", id);

        Setting setting = settingRepository.findOne(id);
        return convertEntityToDto(setting);
    }

    @Override
    public SettingDTO getSettingByCode(String code) {

        logger.debug("Getting setting by code [{}]", code);

        Setting setting = settingRepository.findByCode(code);
        if(setting != null) {
            return convertEntityToDto(setting);
        }
        return null;
    }

    @Override
    public String deleteSetting(Long id) {

        logger.debug("Deleting setting with Id {}", id);

        Setting setting = settingRepository.findOne(id);

        try {
            settingRepository.delete(setting);
            logger.warn("Deleted setting {}", setting);
            return messageSource.getMessage("setting.delete.success", null, locale);
        }
        catch (Exception e){
            logger.error("Failed to delete setting {}", setting, e);
            throw new GravityException(messageSource.getMessage("setting.delete.failure", null, locale),e);
        }
    }

    @Override
    public String enableSetting(Long id) {

        logger.debug("Enabling setting with Id {}", id);

        Setting setting = settingRepository.findOne(id);
        setting.setEnabled(true);

        try {
            settingRepository.save(setting);
            logger.info("Enabled setting {}", setting);
            return messageSource.getMessage("setting.enable.success", null, locale);
        }
        catch (Exception e){
            logger.error("Failed to enable setting {}", setting, e);
            throw new GravityException(messageSource.getMessage("setting.enable.failure", null, locale),e);
        }
    }

    @Override
    public String disableSetting(Long id) {

        logger.debug("Disabling setting with Id {}", id);

        Setting setting = settingRepository.findOne(id);
        setting.setEnabled(false);

        try {
            settingRepository.save(setting);
            logger.info("Disabled setting {}", setting);
            return messageSource.getMessage("setting.disable.success", null, locale);
        }
        catch (Exception e){
            logger.error("Failed to disable setting {}", setting, e);
            throw new GravityException(messageSource.getMessage("setting.disable.failure", null, locale), e);
        }
    }


    @Override
    public boolean isSettingAvailable(String code){
        boolean check = true ;
        logger.info("checking ->{} configuration",code);
        Setting setting = settingRepository.findByCode(code);
        if(Objects.nonNull(setting)){
            if(setting.isEnabled()){
                check = true ;
            }else {
                check = false ;
            }
        }else {
            check = false ;
        }
        logger.debug("->{} ,->{}", code , setting.isEnabled());
        return check ;
    }

    @Override
    public boolean exists(String code) {
        return settingRepository.existsByCode(code);
    }

    @Override
    public Page<SettingDTO> getAllSettings(Pageable pageable) {

        logger.debug("Retrieving system settings");

        Page<Setting> settings = settingRepository.findAll(pageable);
        List<SettingDTO> settingDTOs = convertEntitiesToDtos(settings.getContent());
        return new PageImpl<>(settingDTOs,pageable, settings.getTotalElements());
    }

    @Override
    public Page<SettingDTO> findSettings(String pattern, Pageable pageDetails) {
        Page<Setting> page = settingRepository.findUsingPattern(pattern, pageDetails);
        List<SettingDTO> dtOs = convertEntitiesToDtos(page.getContent());
        Page<SettingDTO> pageImpl = new PageImpl<SettingDTO>(dtOs, pageDetails, page.getTotalElements());
        return pageImpl;
    }

    @Override
    public List<SettingDTO> findSettingsLike(String pattern) {
        List<Setting> settings = settingRepository.findByCodeContains(pattern);
        List<SettingDTO> dtOs = convertEntitiesToDtos(settings);
        return dtOs;
    }

    private SettingDTO convertEntityToDto(Setting setting){

        SettingDTO settingDTO = modelMapper.map(setting, SettingDTO.class);
        return settingDTO;
    }

    private Setting convertDtoToEntity(SettingDTO settingDTO){

        Setting setting = modelMapper.map(settingDTO, Setting.class);
        return setting;
    }


    private List<SettingDTO> convertEntitiesToDtos(List<Setting> settings){

        return settings.stream().map(setting -> convertEntityToDto(setting)).collect(Collectors.toList());
    }
}




package com._3line.gravity.core.setting.service;

import com._3line.gravity.core.setting.dto.SettingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by FortunatusE on 8/3/2018.
 */
public interface SettingService {

    /**
     * Adds the given system setting to the database
     * @param settingDTO the setting to be added
     * @return success message
     */
//    @PreAuthorize("hasAuthority('ADD_SETTING')")
    String addSetting(SettingDTO settingDTO);


    /**
     * Updates the given system setting
     * @param settingDTO the setting to be updated
     * @return success message
     */
//    @PreAuthorize("hasAuthority('UPDATE_SETTING')")
    String updateSetting(SettingDTO settingDTO);


    /**
     * Returns the setting specified by the given Id
     * @param id the setting's Id
     * @return the setting
     */
    @PreAuthorize("hasAuthority('GET_SETTING')")
    SettingDTO getSettingById(Long id);

    /**
     * Returns the setting specified by the given name
     * @param code the setting's name
     * @return the setting
     */
    SettingDTO getSettingByCode(String code);

    /**
     * Deletes the setting specified by the given Id
     * @param id the setting's Id
     * @return success message
     */
    @PreAuthorize("hasAuthority('DELETE_SETTING')")
    String deleteSetting(Long id);

    /*"
     * Check if configuration is available and enabled
     * @param code the settings code
     * @return true if settings is available and enabled
     */
    boolean isSettingAvailable(String code);


    /**
     * Checks if the setting specified by the given code exists
     * @param code the code that identifies the setting
     * @return true if the setting exists
     */
    boolean exists(String code);

    /**
     * Returns all system settings in paged format
     * @param pageable the page parameters
     * @return paged settings
     */
    @PreAuthorize("hasAuthority('VIEW_SETTINGS')")
    Page<SettingDTO> getAllSettings(Pageable pageable);

    @PreAuthorize("hasAuthority('ENABLE_SETTING')")
    String enableSetting(Long id);

    @PreAuthorize("hasAuthority('DISABLE_SETTING')")
    String disableSetting(Long id);

    @PreAuthorize("hasAuthority('VIEW_SETTINGS')")
    Page<SettingDTO> findSettings(String pattern, Pageable pageDetails);

    List<SettingDTO> findSettingsLike(String pattern);
}

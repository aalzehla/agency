package com._3line.gravity.core.usermgt.service;

import com._3line.gravity.core.usermgt.dto.ApplicationUserDTO;
import com._3line.gravity.core.usermgt.dto.UpdatePasswordDTO;
import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.core.usermgt.model.SystemUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ApplicationUserService {


    @PreAuthorize("hasAuthority('CREATE_SYSTEM_USER')")
    public String createSystemUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException;

    void validateNewUser(ApplicationUserDTO userDTO) ;

    @PreAuthorize("hasAuthority('CREATE_BRANCH_USER')")
    public String createBranchUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('UPDATE_SYSTEM_USER')")
    public String updateSystemUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException;

    void validateExistingUser(ApplicationUserDTO userDTO) ;

    @PreAuthorize("hasAuthority('UPDATE_BRANCH_USER')")
    public String updateBranchUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('DELETE_SYSTEM_USER')")
    public String deleteSystemUser(Long userId) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('DELETE_BRANCH_USER')")
    public String deleteBranchUser(Long userId) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('ENABLE_SYSTEM_USER')")
    public String enableSystemUser(Long userId) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('ENABLE_BRANCH_USER')")
    public String enableBranchUser(Long userId) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('DISABLE_SYSTEM_USER')")
    public String disableSystemUser(Long userId) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('DISABLE_BRANCH_USER')")
    public String disableBranchUser(Long userId) throws AppUserServiceException;

    //    @PreAuthorize("hasAuthority('RESET_PASSWORD')")
    public String resetPassword(Long userId) throws AppUserServiceException;

    public String updatePassword(UpdatePasswordDTO updatePasswordDTO) throws AppUserServiceException;

    public void loginUser(Long userId) throws AppUserServiceException;

    public void logoutUser(Long userId) throws AppUserServiceException;

    public ApplicationUserDTO findByUsername(String username) throws AppUserServiceException;

    public SystemUser findUserByUsername(String username) throws AppUserServiceException;

    @PreAuthorize("hasAuthority('VIEW_SYSTEM_USERS')")
    Page<ApplicationUserDTO> getSystemUsers(Pageable pageable);

    @PreAuthorize("hasAuthority('VIEW_BRANCH_USERS')")
    Page<ApplicationUserDTO> getBranchUsers(Pageable pageable);

    @PreAuthorize("hasAuthority('GET_USER')")
    ApplicationUserDTO getUser(Long userId);

    public ApplicationUserDTO findByEmail(String email) throws AppUserServiceException;

    public SystemUser convertDtoToEntity(ApplicationUserDTO applicationUserDTO);

    public ApplicationUserDTO convertEntityToDto(SystemUser systemUser);


    Page<ApplicationUserDTO> findSystemUsers(String pattern, Pageable pageDetails);

    Page<ApplicationUserDTO> findBranchUsers(String pattern, Pageable pageDetails);
}

package com._3line.gravity.core.usermgt.service.implementation;

import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.notification.NotificationService;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.rolemgt.models.RoleType;
import com._3line.gravity.core.rolemgt.repositories.RoleRepository;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.usermgt.dto.ApplicationUserDTO;
import com._3line.gravity.core.usermgt.dto.UpdatePasswordDTO;
import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.utils.EmailValidator;
import com._3line.gravity.core.utils.PhoneNumberValidator;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.utility.Utility;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private MessageSource messageSource;
    private Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SettingService settingService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CodeService codeService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Qualifier("IPIntgratedSMSImplementation")
    @Autowired
    private SmsService smsService;



    @RequireApproval(code = "CREATE_USER" , entityType = SystemUser.class)
    @Override
    public String createSystemUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException {

        logger.debug("Creating system user: {}", applicationUserDTO);

        try {
            SystemUser systemUser = convertDtoToEntity(applicationUserDTO);
            String password = "";
            if (Objects.nonNull(applicationUserRepository.findByUserName(systemUser.getUserName()))) {
                throw new AppUserServiceException(messageSource.getMessage("user.add.exist", null, locale));
            }
            // TODO check if active directory is enabled
            if (settingService.isSettingAvailable("ACTIVE_DIRECTORY_INTEGRATION")) {
                //TODO check if user exists in directory
                throw new AppUserServiceException(messageSource.getMessage("user.add.add.notfound", null, locale));
            }

            if (settingService.isSettingAvailable("USER_CREATION_GENERATE_PASSWORD")) {
                password = "FG" + Utility.randomString(10) + "@";
                systemUser.setPassword(passwordEncoder.encode(password));
                systemUser.setChangePassword(true);
                systemUser.setNoOfWrongLoginCount(0);
            }

            SystemUser newUser = applicationUserRepository.save(systemUser);
            logger.info("New user [{}] created", newUser.getUserName());
            notificationService.sendUserCreationMessage(newUser,password);
            return messageSource.getMessage("user.add.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.add.failure", null, locale), e);
        }

    }

    @Override
    public String createBranchUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException {

        logger.debug("Creating branch user: {}", applicationUserDTO);

        validateNewUser(applicationUserDTO);
        return createUser(applicationUserDTO);


    }

    @RequireApproval(code = "CREATE_USER" , entityType = SystemUser.class)
    private String createUser(ApplicationUserDTO applicationUserDTO){
       return "";
    }

    public void validateNewUser(ApplicationUserDTO userDTO){

        logger.debug("Validating new user: {}", userDTO);

            SystemUser systemUser = applicationUserRepository.findByUserName(userDTO.getUserName());

            if(systemUser != null){
                //Username already exists
                throw new AppUserServiceException("Username ["+userDTO.getUserName()+"] already exists");
            }

            systemUser = applicationUserRepository.findByEmail(userDTO.getEmail());

            if(systemUser != null){
                //Email address already exists
                throw new AppUserServiceException("Email address ["+userDTO.getEmail()+"] already exists");
            }

            validateContactDetails(userDTO);

    }

    @RequireApproval(code = "UPDATE_USER" , entityType = SystemUser.class)
    @Override
    public String updateSystemUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException {

        try {
            SystemUser systemUser = applicationUserRepository.findOne(applicationUserDTO.getId());
            systemUser.setVersion(applicationUserDTO.getVersion());
            systemUser.setFirstName(applicationUserDTO.getFirstName());
            systemUser.setLastName(applicationUserDTO.getLastName());
            systemUser.setEmail(applicationUserDTO.getEmail());
            systemUser.setPhoneNumber(applicationUserDTO.getPhoneNumber());
            systemUser.setRole(roleRepository.findOne(applicationUserDTO.getRoleId()));
            if (applicationUserDTO.getBranchId() != null) {
                systemUser.setBranch(codeService.getCodeById(applicationUserDTO.getBranchId()));
            }
            SystemUser updatedUser = applicationUserRepository.save(systemUser);
            notificationService.sendUserUpdateMessage(updatedUser);
            logger.info("Updated user [{}]", updatedUser.getUserName());
            return messageSource.getMessage("user.update.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.update.failure", null, locale), e);
        }
    }


    @Override
    public String updateBranchUser(ApplicationUserDTO applicationUserDTO) throws AppUserServiceException {

        logger.debug("Updating branch user:  {}", applicationUserDTO);
        validateExistingUser(applicationUserDTO);
        return updateUser(applicationUserDTO);
    }


    private String updateUser(ApplicationUserDTO applicationUserDTO){

        return "";
    }


    public void validateExistingUser(ApplicationUserDTO userDTO){

        logger.debug("Validating existing user: {}", userDTO);

        SystemUser systemUser = applicationUserRepository.findByUserName(userDTO.getUserName());

        if(systemUser != null && !systemUser.getId().equals(userDTO.getId())){
            //Username already exists
            throw new AppUserServiceException("Username ["+userDTO.getUserName()+"] already exists");
        }

        systemUser = applicationUserRepository.findByEmail(userDTO.getEmail());

        if(systemUser != null && !systemUser.getId().equals(userDTO.getId())){
            //Email address already exists
            throw new AppUserServiceException("Email address ["+userDTO.getEmail()+"] already exists");
        }
        validateContactDetails(userDTO);

    }




    private void validateContactDetails(ApplicationUserDTO userDTO){

        if(!EmailValidator.isValid(userDTO.getEmail())){
            //Invalid email address
            throw new AppUserServiceException("Invalid email address ["+userDTO.getEmail()+"]");

        }

        if(userDTO.getPhoneNumber()!=null && !PhoneNumberValidator.isValid(userDTO.getPhoneNumber())){
            //Invalid phone number
            throw new AppUserServiceException("Invalid phone number ["+userDTO.getPhoneNumber()+"]");
        }
    }


    @Override
    public String deleteSystemUser(Long userId) throws AppUserServiceException {

        logger.debug("Deleting  system user with Id [{}]", userId);
        return  deleteUser(userId);

    }

    @Override
    public String deleteBranchUser(Long userId) throws AppUserServiceException {

        logger.debug("Deleting branch user with Id [{}]", userId);
        return  deleteUser(userId);

    }

    private String deleteUser(Long userId){

        try {
            applicationUserRepository.delete(userId);
            logger.warn("Deleted user with Id [{}]", userId);
            return messageSource.getMessage("user.delete.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.delete.failure", null, locale), e);
        }

    }

    @Override
    public String enableSystemUser(Long userId) throws AppUserServiceException {

        logger.debug("Enabling system user [{}]", userId);
        return enableUser(userId);
    }

    @Override
    public String enableBranchUser(Long userId) throws AppUserServiceException {

        logger.debug("Enabling branch user [{}]", userId);
        return enableUser(userId);
    }

    private String enableUser(Long userId){
        try {
            SystemUser systemUser = applicationUserRepository.findOne(userId);
            systemUser.setEnabled(true);
            applicationUserRepository.save(systemUser);
            return messageSource.getMessage("user.enable.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.enable.failure", null, locale), e);
        }
    }

    @Override
    public String disableSystemUser(Long userId) throws AppUserServiceException {

        logger.debug("Disabling system user [{}]", userId);
        return disableUser(userId);
    }

    @Override
    public String disableBranchUser(Long userId) throws AppUserServiceException {

        logger.debug("Disabling branch user [{}]", userId);
        return disableUser(userId);
    }

    private String disableUser(Long userId){
        try {
            SystemUser systemUser = applicationUserRepository.findOne(userId);
            systemUser.setEnabled(false);
            applicationUserRepository.save(systemUser);
            return messageSource.getMessage("user.disable.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.disable.failure", null, locale), e);
        }
    }


    @Override
    public String resetPassword(Long userId) throws AppUserServiceException {
        if (settingService.isSettingAvailable("USER_PASSWORD_RESET")) {
            try {
                String password = "";
                SystemUser systemUser = applicationUserRepository.findOne(userId);
                systemUser.setEnabled(true);
                //TODO generate new password ;
                password = "FG" + Utility.randomString(10) + "@";
                systemUser.setChangePassword(true);
                systemUser.setPassword(passwordEncoder.encode(password));
                applicationUserRepository.save(systemUser);
                if (settingService.isSettingAvailable("USER_PASSWORD_RESET_SEND_EMAIL")) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", systemUser.getFirstName() + " " + systemUser.getLastName());
                    params.put("password", password);
                    params.put("username", systemUser.getUserName());
                    mailService.sendMail("Your password has been reset ", systemUser.getEmail(), null, params, "agent_password_update");

                }

                if (settingService.isSettingAvailable("USER_PASSWORD_RESET_SEND_SMS")) {
                    String message = "Dear "+systemUser.getFirstName() + " " + systemUser.getLastName()+" , " +
                            "your password has been reset new password is "+password+" , keep using freedom !";
                    smsService.sendPlainSms(systemUser.getPhoneNumber(),message);

                }

                return messageSource.getMessage("user.password.reset.success", null, locale);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AppUserServiceException(messageSource.getMessage("user.password.reset.failure", null, locale), e);
            }
        } else {
            throw new AppUserServiceException(messageSource.getMessage("user.password.reset.disabled", null, locale));
        }
    }

    @Override
    public String updatePassword(UpdatePasswordDTO updatePasswordDTO) throws AppUserServiceException {
        if (settingService.isSettingAvailable("USER_PASSWORD_UPDATE")) {
            try {
                SystemUser systemUser = applicationUserRepository.findOne(updatePasswordDTO.getId());
                if(!passwordEncoder.matches(updatePasswordDTO.getOldPassword() ,systemUser.getPassword())){
                    logger.info("old password does not match");
                    throw new AppUserServiceException("Password does not match");
                }
                systemUser.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));

                systemUser.setEnabled(true);
                systemUser.setChangePassword(false);
                applicationUserRepository.save(systemUser);
                if (settingService.isSettingAvailable("USER_PASSWORD_UPDATE_SEND_EMAIL")) {
                    //TODO SEND EMAIL ON CREATION
                }

                if (settingService.isSettingAvailable("USER_PASSWORD_UPDATE_SEND_SMS")) {
                    //TODO SEND SMS ON CREATION
                }

                return messageSource.getMessage("user.password.update.success", null, locale);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AppUserServiceException(messageSource.getMessage("user.update.reset.failure", null, locale), e);
            }
        } else {
            throw new AppUserServiceException(messageSource.getMessage("user.password.update.disabled", null, locale));
        }
    }

    @Override
    public void loginUser(Long userId) throws AppUserServiceException {
        try {
            SystemUser systemUser = applicationUserRepository.findOne(userId);
            systemUser.setLastLoginDate(new Date());
            systemUser.setLoggedOn(true);
            applicationUserRepository.save(systemUser);
            // TODO add to logged in user service

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.logon.record.failure", null, locale), e);
        }

    }

    @Override
    public void logoutUser(Long userId) throws AppUserServiceException {
        try {
            SystemUser systemUser = applicationUserRepository.findOne(userId);
            systemUser.setLoggedOn(false);
            applicationUserRepository.save(systemUser);
            // TODO add to loggout in user service

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.logout.record.failure", null, locale), e);
        }
    }

    @Override
    public ApplicationUserDTO findByUsername(String username) throws AppUserServiceException {


        try {
            SystemUser systemUser = applicationUserRepository.findByUserName(username);

            return convertEntityToDto(systemUser);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.find.failure", null, locale), e);
        }
    }

    @Override
    public SystemUser findUserByUsername(String username) throws AppUserServiceException {

        logger.debug("Retrieving user [{}]", username);
        try {
            SystemUser systemUser = applicationUserRepository.findByUserName(username);
            return systemUser;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.find.failure", null, locale), e);
        }
    }

    @Override
    public ApplicationUserDTO findByEmail(String email) throws AppUserServiceException {

        try {
            SystemUser systemUser = applicationUserRepository.findByEmail(email);

            return convertEntityToDto(systemUser);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.find.failure", null, locale), e);
        }
    }


    @Override
    public ApplicationUserDTO convertEntityToDto(SystemUser systemUser) {

        ApplicationUserDTO applicationUserDTO = modelMapper.map(systemUser, ApplicationUserDTO.class);
        if (Objects.nonNull(systemUser.getLastLoginDate())) {
            applicationUserDTO.setLastLogin(DateUtil.FIformatDateToreadable_(systemUser.getLastLoginDate()));
        }
        applicationUserDTO.setFullName(systemUser.getFirstName() + " " + systemUser.getLastName());
        Role userRole = systemUser.getRole();
        applicationUserDTO.setRoleId(userRole.getId());
        applicationUserDTO.setRoleName(userRole.getName());
        applicationUserDTO.setRoleType(userRole.getRoleType().name());
        if (systemUser.getBranch() != null) {
            applicationUserDTO.setBranchId(systemUser.getBranch().getId());
            applicationUserDTO.setBranchName(systemUser.getBranch().getDescription());
        }
        return applicationUserDTO;
    }

    @Override
    public SystemUser convertDtoToEntity(ApplicationUserDTO applicationUserDTO) {

        SystemUser systemUser = modelMapper.map(applicationUserDTO, SystemUser.class);
        systemUser.setRole(roleRepository.findOne(applicationUserDTO.getRoleId()));
        if (applicationUserDTO.getBranchId() != null) {
            systemUser.setBranch(codeService.getCodeById(applicationUserDTO.getBranchId()));
        }
        return systemUser;
    }


    private List<ApplicationUserDTO> convertEntitiesToDtos(List<SystemUser> systemUsers) {

        return systemUsers.stream().map(applicationUser -> convertEntityToDto(applicationUser)).collect(Collectors.toList());
    }


    @Override
    public Page<ApplicationUserDTO> getSystemUsers(Pageable pageable) {

        Page<SystemUser> users = applicationUserRepository.findByRole_RoleType(RoleType.SYSTEM, pageable);
        return new PageImpl<ApplicationUserDTO>(convertEntitiesToDtos(users.getContent()), pageable, users.getTotalElements());
    }

    @Override
    public Page<ApplicationUserDTO> getBranchUsers(Pageable pageable) {

        Page<SystemUser> users = applicationUserRepository.findByRole_RoleType(RoleType.BRANCH, pageable);
        return new PageImpl<ApplicationUserDTO>(convertEntitiesToDtos(users.getContent()), pageable, users.getTotalElements());
    }

    @Override
    public ApplicationUserDTO getUser(Long userId) {

        logger.debug("Retrieving user [{}]", userId);
        SystemUser systemUser = applicationUserRepository.findOne(userId);
        return convertEntityToDto(systemUser);
    }

    @Override
    public Page<ApplicationUserDTO> findSystemUsers(String pattern, Pageable pageDetails) {
        Page<SystemUser> page = applicationUserRepository.findUsingPattern(pattern, pageDetails);
        List<SystemUser> systemUsers = page.stream().filter(applicationUser -> applicationUser.getRole().getRoleType().equals(RoleType.SYSTEM)).collect(Collectors.toList());
        List<ApplicationUserDTO> dtOs = convertEntitiesToDtos(systemUsers);
        Page<ApplicationUserDTO> pageImpl = new PageImpl<ApplicationUserDTO>(dtOs, pageDetails, systemUsers.size());
        return pageImpl;
    }

    @Override
    public Page<ApplicationUserDTO> findBranchUsers(String pattern, Pageable pageDetails) {
        Page<SystemUser> page = applicationUserRepository.findUsingPattern(pattern, pageDetails);
        List<SystemUser> branchUsers = page.stream().filter(applicationUser -> applicationUser.getRole().getRoleType().equals(RoleType.BRANCH)).collect(Collectors.toList());
        List<ApplicationUserDTO> dtOs = convertEntitiesToDtos(branchUsers);
        Page<ApplicationUserDTO> pageImpl = new PageImpl<ApplicationUserDTO>(dtOs, pageDetails, branchUsers.size());
        return pageImpl;
    }
}

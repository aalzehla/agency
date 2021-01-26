package com._3line.gravity.core.setup;


import com._3line.gravity.core.rolemgt.dtos.PermissionDTO;
import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
import com._3line.gravity.core.rolemgt.models.Permission;
import com._3line.gravity.core.rolemgt.models.PermissionType;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.rolemgt.repositories.PermissionRepository;
import com._3line.gravity.core.rolemgt.repositories.RoleRepository;
import com._3line.gravity.core.rolemgt.service.RoleService;
import com._3line.gravity.core.setting.model.Setting;
import com._3line.gravity.core.setting.repository.SettingRepository;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SetupConfiguration  implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        /*
         * Load default settings variable from default-config file
         */
        logger.info("######### INITIALIZING CORE CONFIGURATIONS #########");
        List<Setting> configs = getAllSettingsFromFile("default-configuration.properties");

        configs.forEach( setting -> {
            try {
                if(!settingRepository.existsByCode(setting.getCode())) {
                    settingRepository.save(setting);
                }

            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        });

        scanAndCreatePermissions();

        if(shouldCreateDefaultSuperAdmin()){
            createDefaultSuperAdminRoleAndUser();
        }
    }

    public List<Setting> getAllSettingsFromFile(String file){
        InputStream input = null;
        Properties prop = new Properties();
        List<Setting> settings = new ArrayList<>();
        try {

            input = getClass().getClassLoader().getResourceAsStream(file);
            // load a properties file
            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                Setting setting = new Setting();
                setting.setName(key);
                setting.setCode(key);
                setting.setValue(value);
                if(key.equalsIgnoreCase("ACTIVE_DIRECTORY_INTEGRATION")){
                    setting.setEnabled(false);
                }else{
                    setting.setEnabled(true);
                }

                settings.add(setting);
            }

        } catch (NullPointerException ex) {
            logger.info("Null pointer exception\n");
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.out.printf("File name %s was not found\n", file);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.printf("File name %s caused IO exception\n", file);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return settings;
    }

    private void scanAndCreatePermissions(){
        logger.debug("Scanning permission annotations");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com._3line.gravity.core"))
                .setScanners(new MethodAnnotationsScanner()));

                Set<Method> methods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);

                for (Method method : methods) {

                        PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                        String value = preAuthorize.value();
                        String name = StringUtils.substringBetween(value, "hasAuthority('", "')");
                        logger.debug("Permission: {}", name);

                    boolean permissionExists = permissionRepository.existsByName(name);

                    if(!permissionExists){
                        Permission permission =  new Permission();
                        permission.setName(name);
                        permission.setPermissionType(PermissionType.BOTH);
                        permissionRepository.save(permission);
                    }
                }
    }


    @Transactional
    private void createDefaultSuperAdminRoleAndUser(){

            logger.info("Creating default super admin role and user");

            try {
                Role role = roleRepository.findFirstByNameIgnoreCase("Super Admin");
                if (role == null) {
                    logger.info("No super admin role found, will try to create one");
                    Iterable<Permission> permissions = permissionRepository.findAll();
                    if (Iterables.isEmpty(permissions)) {
                        logger.warn("No permissions found, cannot create role without permissions");
                        logger.info("Aborted creation of super admin");
                        return;
                    }
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setName("Super Admin");
                    roleDTO.setType("SYSTEM");
                    roleDTO.setDescription("This is the default super admin role that has all the permissions");
                    roleDTO.setPermissions(StreamSupport.stream(permissions.spliterator(), false).map(permission -> convertEntityToDto(permission)).collect(Collectors.toList()));
                    role = roleService.createRole(roleDTO);
                    logger.info("Created super admin role [{}]", role.getName());
                }

                SystemUser superAdmin = new SystemUser();
                superAdmin.setUserName("superadmin");
                superAdmin.setFirstName("Super");
                superAdmin.setLastName("Admin");
                superAdmin.setEmail("softwaredev@3lineng.com");
                superAdmin.setRole(role);
                superAdmin.setPassword(passwordEncoder.encode("superadmin"));
                userRepository.save(superAdmin);

                SystemUser superVerify = new SystemUser();
                superVerify.setUserName("superverify");
                superVerify.setFirstName("Super");
                superVerify.setLastName("Admin");
                superVerify.setEmail("rasaq.agbalaya@3lineng.com");
                superVerify.setRole(role);
                superVerify.setPassword(passwordEncoder.encode("superverify"));
                userRepository.save(superVerify);
                logger.info("Super admin users created");
            }
            catch (Exception e){
                logger.error("Failed to create super admin", e);
            }

    }

    private PermissionDTO convertEntityToDto(Permission permission){

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permission.getId());
        return permissionDTO;
    }


    private boolean shouldCreateDefaultSuperAdmin(){

        logger.info("Checking if super admin user should be created");
        Setting setting = settingRepository.findByCode("CREATE_DEFAULT_SUPER_ADMIN");

        if(setting != null && setting.isEnabled() && "YES".equalsIgnoreCase(setting.getValue())){

            SystemUser check = userRepository.findByUserName("superadmin");
            boolean superAdminExists = check==null?false:true;
            if(!superAdminExists) {
                logger.info("Super admin will be created");
                return true;
            }
            logger.info("Super admin already exists");
        }
        logger.info("Super admin will not be created");
        return false;
    }



}

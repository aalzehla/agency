package com._3line.gravity.core.rolemgt.service.implementation;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.rolemgt.dtos.PermissionDTO;
import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
import com._3line.gravity.core.rolemgt.models.Permission;
import com._3line.gravity.core.rolemgt.models.PermissionType;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.rolemgt.models.RoleType;
import com._3line.gravity.core.rolemgt.repositories.PermissionRepository;
import com._3line.gravity.core.rolemgt.repositories.RoleRepository;
import com._3line.gravity.core.rolemgt.service.RoleService;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com.google.common.collect.Lists;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private PermissionRepository permissionRepo;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private ApplicationUserRepository userRepo;

    private ModelMapper modelMapper = new ModelMapper();

    private Locale locale = LocaleContextHolder.getLocale();

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public String addRole(RoleDTO roleDTO) throws GravityException {

        Role role = roleRepo.findFirstByNameIgnoreCase(roleDTO.getName());

        if (role != null) {
            throw new GravityException(messageSource.getMessage("role.exist", null, locale));
        }

        try {
            logger.info("Added role before save call");
            role = convertDTOToEntity(roleDTO);
            roleRepo.save(role);
            logger.info("Added role {}", role.toString());
            return messageSource.getMessage("role.add.success", null, locale);
        }
       catch (GravityException e) {
            throw new GravityException(messageSource.getMessage("role.add.failure", null, locale), e);
        }
    }

    @Override
    @Transactional
    public Role createRole(RoleDTO roleDTO) {
        Role role = roleRepo.findFirstByNameIgnoreCase(roleDTO.getName());

        if (role != null) {
            throw new GravityException(messageSource.getMessage("role.exist", null, locale));
        }

        try {
            role = convertDTOToEntity(roleDTO);
            roleRepo.save(role);
            logger.info("Added role {}", role.toString());
            return role;
        }
        catch (GravityException e) {
            throw new GravityException(messageSource.getMessage("role.add.failure", null, locale), e);
        }
    }


    @Override
    public RoleDTO getRole(Long id) {
        Role role = roleRepo.findOne(id);
        return convertEntityToDTO(role);
    }

    @Override
    public Role getRole(String roleName) {
        Role role = roleRepo.findFirstByNameIgnoreCase(roleName);
        return role;
    }


    @Override
    public List<RoleDTO> getRoles() {
        List<Role> roles = new ArrayList<>();
        for (Role r:roleRepo.findAll()) {
            roles.add(r);
        }
        return convertRoleEntitiesToDTOs(roles);
    }

    @Override
    public String updateRole(RoleDTO roleDTO) throws GravityException {

        Role role = roleRepo.findByNameAndRoleType(roleDTO.getName(), RoleType.valueOf(roleDTO.getType()));

        if (role != null && !roleDTO.getId().equals(role.getId())) {
            throw new GravityException(messageSource.getMessage("role.exists", null, locale));

        }
        try {
            role = convertDTOToEntity(roleDTO);
              roleRepo.save(role);

            return messageSource.getMessage("role.update.success", null, locale);
        }
        catch (Exception e) {
            throw new GravityException(messageSource.getMessage("role.update.failure", null, locale), e);

        }
    }

    @Override
    public String deleteRole(Long id) throws GravityException {

        Role role = roleRepo.findOne(id);
        Integer users = countUsers(role);
        if (users > 0) {
            throw new GravityException(messageSource.getMessage("role.delete.users.exist", null, locale));
        }
        try {


            role.setDelFlag("Y");
            roleRepo.delete(role);
            return messageSource.getMessage("role.delete.success", null, locale);
        } catch (GravityException e) {
            throw new GravityException(messageSource.getMessage("role.delete.failure", null, locale), e);

        }
    }

    @Override
    public String addPermission(PermissionDTO permissionDTO) throws GravityException {
        try {
            Permission permission = convertDTOToEntity(permissionDTO);
            permissionRepo.save(permission);
            logger.info("Added permission {}", permission.toString());
            return messageSource.getMessage("permission.add.success", null, locale);
        }
        catch (Exception e) {
            throw new GravityException(messageSource.getMessage("permission.add.failure", null, locale), e);
        }
    }

    @Override
    public boolean permissionExists(String name) {
       return permissionRepo.existsByName(name);
    }

    @Override
    public PermissionDTO getPermission(Long id) {
        Permission permission = permissionRepo.findOne(id);
        return convertEntityToDTO(permission);
    }

    @Override
    public Collection<PermissionDTO> getPermissions() {
        Iterable<Permission> permissions = permissionRepo.findAll();
        return convertPermissionEntitiesToDTOs(permissions);
    }

    @Override
    public List<Permission> getAllPermissions() {
        Iterable<Permission> permissions = permissionRepo.findAll();
        return Lists.newArrayList(permissions);
    }

    @Override
    public Page<RoleDTO> getRoles(Pageable pageDetails) {
        Page<Role> page = roleRepo.findAll(pageDetails);
        logger.debug("Total roles: {}", page.getTotalElements());
        List<RoleDTO> dtOs = convertRoleEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<RoleDTO> pageImpl = new PageImpl<RoleDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }

    @Override
    public List<RoleDTO> getSystemRoles() {

        List<Role> roles = roleRepo.findByRoleType(RoleType.SYSTEM);
        return convertRoleEntitiesToDTOs(roles);
    }

    @Override
    public List<RoleDTO> getBranchRoles() {
        List<Role> roles = roleRepo.findByRoleType(RoleType.BRANCH);
        return convertRoleEntitiesToDTOs(roles);
    }

    @Override
    public Page<PermissionDTO> getPermissions(Pageable pageDetails) {
        Page<Permission> page = permissionRepo.findAll(pageDetails);
        List<PermissionDTO> dtOs = convertPermissionEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<PermissionDTO> pageImpl = new PageImpl<PermissionDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }

    @Override
    public String updatePermission(PermissionDTO permissionDTO) throws GravityException {
        try {
            Permission permission = permissionRepo.findOne(permissionDTO.getId());
            permission.setVersion(permissionDTO.getVersion());
            permission.setName(permissionDTO.getName());
            permission.setDescription(permissionDTO.getDescription());
            permission.setPermissionType(PermissionType.valueOf(permissionDTO.getType()));
            permissionRepo.save(permission);
            logger.info("Updated permission {}", permission.toString());
            return messageSource.getMessage("permission.update.success", null, locale);
        }
        catch (GravityException e){
            throw e;
        }
        catch (Exception e) {
            throw new GravityException(messageSource.getMessage("permission.add.failure", null, locale), e);
        }
    }

    @Override
    public String deletePermission(Long id) throws GravityException {
        try {
            Permission permission = permissionRepo.findOne(id);
            permissionRepo.delete(permission);
            logger.warn("Deleted permission with Id {}", id);
            return messageSource.getMessage("permission.delete.success", null, locale);
        }
        catch (GravityException e){
            throw e;
        }
        catch (Exception e) {
            throw new GravityException(messageSource.getMessage("permission.delete.failure", null, locale), e);
        }
    }

    public RoleDTO convertEntityToDTO(Role role) {
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        roleDTO.setType(role.getRoleType().toString());
        return modelMapper.map(role, RoleDTO.class);
    }

    private Role convertDTOToEntity(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        role.setRoleType(RoleType.valueOf(roleDTO.getType()));
        role.setPermissions(convertPermissionDTOsToEntities(roleDTO.getPermissions()));
        return role;
    }

    private List<RoleDTO> convertRoleEntitiesToDTOs(List<Role> roles) {
        List<RoleDTO> roleDTOList = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
                roleDTOList.add(roleDTO);

        }
        return roleDTOList;
    }

    private PermissionDTO convertEntityToDTO(Permission permission) {
        PermissionDTO permissionDTO =   modelMapper.map(permission, PermissionDTO.class);
        permissionDTO.setType(permission.getPermissionType().toString());
        return permissionDTO;
    }

    private Permission convertDTOToEntity(PermissionDTO permissionDTO) {
        Permission permission;
        if (permissionDTO.getId() == null) {
            permission = modelMapper.map(permissionDTO, Permission.class);
            permission.setPermissionType(PermissionType.valueOf(permissionDTO.getType()));

        } else {
            permission = permissionRepo.findOne(permissionDTO.getId());
            if(permissionDTO.getName()!=null) {
                permission.setName(permissionDTO.getName());
            }
            if(permissionDTO.getDescription()!=null) {
                permission.setDescription(permissionDTO.getDescription());
            }
            if(permissionDTO.getType()!=null){
                permission.setPermissionType(PermissionType.valueOf(permissionDTO.getType()));
            }
        }

        return permission;
    }

    private List<PermissionDTO> convertPermissionEntitiesToDTOs(Iterable<Permission> permissions) {
        List<PermissionDTO> permissionDTOList = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionDTO permissionDTO = convertEntityToDTO(permission);
            permissionDTOList.add(permissionDTO);
        }
        return permissionDTOList;
    }

    private List<Permission> convertPermissionDTOsToEntities(Iterable<PermissionDTO> permissionDTOs) {
        List<Permission> permissions = new ArrayList<>();
        for (PermissionDTO permissionDTO : permissionDTOs) {
            Permission permission = convertDTOToEntity(permissionDTO);
            permissions.add(permission);
        }
        return permissions;
    }

    @Override
    public Iterable<PermissionDTO> getPermissionsNotInRole(RoleDTO role) {
        // TODO Auto-generated method stub
        Long[] permissionArray = new Long[role.getPermissions().size()];
        int idx = 0;
        for (PermissionDTO perm : role.getPermissions()) {
            permissionArray[idx] = perm.getId();
            idx++;
        }
        //not in NULL check
        if (permissionArray.length == 0)
            permissionArray = new Long[]{-1L};
        Iterable<Permission> permissionsNotInRole = permissionRepo.findByIdNotIn(permissionArray);

        logger.debug("Permissions not in role: "+permissionsNotInRole);
        return convertPermissionEntitiesToDTOs(permissionsNotInRole);
    }

    @Override
    public Page<SystemUser> getPersons(RoleDTO roledto, Pageable pageDetails) {
        Role role = roleRepo.findOne(roledto.getId());
        Page<SystemUser> pageImpl = null;

        Page<SystemUser> users = userRepo.findByRole(role, pageDetails);
        long elements = users.getTotalElements();
        List<SystemUser> userList = (List<SystemUser>) (List<?>) users.getContent();
        pageImpl = new PageImpl<SystemUser>(userList, pageDetails, elements);

        return pageImpl;
    }

    private Integer countUsers(Role role) {
        Integer cnt = 0;
        cnt = userRepo.countByRole(role);
        return cnt;
    }

    @Override
    public Page<RoleDTO> findRoles(String pattern, Pageable pageDetails) {
        Page<Role> page = roleRepo.findUsingPattern(pattern, pageDetails);
        List<RoleDTO> dtOs = convertRoleEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<RoleDTO> pageImpl = new PageImpl<RoleDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }

    @Override
    public Page<PermissionDTO> findPermissions(String pattern, Pageable pageDetails) {
        Page<Permission> page = permissionRepo.findUsingPattern(pattern, pageDetails);
        List<PermissionDTO> dtOs = convertPermissionEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<PermissionDTO> pageImpl = new PageImpl<PermissionDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }
}

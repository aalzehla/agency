package com._3line.gravity.core.rolemgt.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.rolemgt.dtos.PermissionDTO;
import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
import com._3line.gravity.core.rolemgt.models.Permission;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.usermgt.model.SystemUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    @PreAuthorize("hasAuthority('ADD_ROLE')")
    String addRole(RoleDTO roleDTO) throws GravityException;

    Role createRole(RoleDTO roleDTO);

    @PreAuthorize("hasAuthority('GET_ROLE')")
    RoleDTO getRole(Long id);

    Role getRole(String roleName);

    List<RoleDTO> getRoles();

    List<RoleDTO> getSystemRoles();

    List<RoleDTO> getBranchRoles();

    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    String updateRole(RoleDTO roleDTO) throws GravityException;

    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    String deleteRole(Long id) throws GravityException;

    String addPermission(PermissionDTO permissionDTO) throws GravityException;

    boolean permissionExists(String code);

    @PreAuthorize("hasAuthority('GET_PERMISSION')")
    PermissionDTO getPermission(Long id);

    Collection<PermissionDTO> getPermissions();

    List<Permission> getAllPermissions();

    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    Page<RoleDTO> getRoles(Pageable pageDetails);

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    Page<PermissionDTO> getPermissions(Pageable pageDetails);

    @PreAuthorize("hasAuthority('UPDATE_PERMISSION')")
    String updatePermission(PermissionDTO permissionDTO) throws GravityException;

    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    String deletePermission(Long id) throws GravityException;

    Iterable<PermissionDTO> getPermissionsNotInRole(RoleDTO role);

    @PreAuthorize("hasAuthority('VIEW_ROLE_USERS')")
    Page<SystemUser> getPersons(RoleDTO roledto, Pageable pageDetails);

    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    Page<RoleDTO> findRoles(String pattern, Pageable pageDetails);

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    Page<PermissionDTO> findPermissions(String pattern, Pageable pageDetails);
}

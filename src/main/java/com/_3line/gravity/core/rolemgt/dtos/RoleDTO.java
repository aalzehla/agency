package com._3line.gravity.core.rolemgt.dtos;


import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

public class RoleDTO{

    private Long id;
    private int version;
    @NotEmpty(message = "Name is required")
    private String name;
    private String description;
    @NotEmpty(message = "Type is required")
    private String type;
    private List<PermissionDTO> permissions;
    private Set<String> approvables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getApprovables() {
        return approvables;
    }

    public void setApprovables(Set<String> approvables) {
        this.approvables = approvables;
    }
}

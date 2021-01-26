package com._3line.gravity.core.rolemgt.models;


import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;

@ToString
@Entity
@Where(clause ="del_Flag='N'" )
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role extends AbstractEntity  {

    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.PERSIST)
    @JoinTable(name = "role_permission", joinColumns =
    @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns =
    @JoinColumn(name = "permission_id", referencedColumnName = "id") )
    //@JsonIgnore
    private List<Permission> permissions = new ArrayList<>();


    @ElementCollection
    private Set<String> approvables;

    public JsonSerializer<Role> getSerializer() {
        return new JsonSerializer<Role>() {
            @Override
            public void serialize(Role value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException
            {
                gen.writeStartObject();
                gen.writeStringField("name",value.getName());
                gen.writeStringField("description",value.getDescription());
                List<String > perms  = new ArrayList<>();
                for (Permission p:  value.getPermissions()){
                    perms.add(p.getName()) ;
                }
                gen.writeObjectField("permissions",perms);
                gen.writeEndObject();
            }
        };
    }

    public Set<String> getApprovables() {
        return approvables;
    }

    public void setApprovables(Set<String> approvables) {
        this.approvables = approvables;
    }

    @Override
    public List<String> getDefaultSearchFields() {
        return Arrays.asList("name",  "description") ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}

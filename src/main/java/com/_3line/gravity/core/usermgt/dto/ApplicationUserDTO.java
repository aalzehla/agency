package com._3line.gravity.core.usermgt.dto;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;

@ToString
public class ApplicationUserDTO extends AbstractVerifiableDto implements PrettySerializer {

    @NotEmpty(message = "Username is required")
    private String userName;
    @NotEmpty(message = "First name is required")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    private String fullName;
    @NotEmpty(message = "Email address is required")
    private String email ;
    private String password ;
    private boolean enabled ;
    private String lastLogin ;
    private Integer noOfWrongLoginCount ;
    private String phoneNumber ;
    private boolean changePassword  ;
    private boolean isLoggedOn;
    @NotNull(message = "Role is required")
    private Long roleId ;
    private String roleName;
    private String roleType;
    private Long branchId;
    private String branchName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getNoOfWrongLoginCount() {
        return noOfWrongLoginCount;
    }

    public void setNoOfWrongLoginCount(Integer noOfWrongLoginCount) {
        this.noOfWrongLoginCount = noOfWrongLoginCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public boolean isLoggedOn() {
        return isLoggedOn;
    }

    public void setLoggedOn(boolean loggedOn) {
        isLoggedOn = loggedOn;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public JsonSerializer<ApplicationUserDTO> getSerializer() {
        return new JsonSerializer<ApplicationUserDTO>() {
            @Override
            public void serialize(ApplicationUserDTO value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeStartObject();
                gen.writeStringField("First name",value.getFirstName());
                gen.writeStringField("Last name",value.getLastName());
                gen.writeStringField("User name",value.getUserName());
                gen.writeStringField("Email",value.getEmail());
                gen.writeStringField("Phone number",value.getPhoneNumber());
                gen.writeStringField("Role",value.getRoleName());
                gen.writeEndObject();
            }
        };
    }
}

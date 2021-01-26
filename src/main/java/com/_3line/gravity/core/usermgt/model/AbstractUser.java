package com._3line.gravity.core.usermgt.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.core.rolemgt.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Data
@MappedSuperclass
public abstract class AbstractUser extends AbstractEntity {

    @Column(unique = true)
    protected String userName;

    protected String firstName;

    protected String lastName;

    @Column(unique = true)
    protected String email ;

    protected String password ;

    protected boolean enabled = true;

    protected Date lastLoginDate ;

    protected Integer noOfWrongLoginCount = 0;

    protected String phoneNumber ;

    protected boolean changePassword  = true ;

    protected boolean isLoggedOn;

    @ManyToOne
    protected Role role ;

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

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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


    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("userName", "firstName","lastName", "email");
    }
}

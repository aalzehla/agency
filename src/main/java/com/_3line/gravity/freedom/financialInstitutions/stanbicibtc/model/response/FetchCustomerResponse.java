/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *
 * @author NiyiO
 */
@JsonRootName(value = "otherResponseDetails")
public class FetchCustomerResponse {

    private String bvn;

    private String firstName;

    private String middleName;

    private String lastName;

    private String dateOfBirth;

    private String phoneNumber;

    private String address;

    private String cifId;

    private String gender;

    private String enrollmentBank;

    private String enrollmentBranch;

    private boolean accountOpened;

    private boolean pinSet;

    private boolean pinActive;

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCifId() {
        return cifId;
    }

    public void setCifId(String cifId) {
        this.cifId = cifId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEnrollmentBank() {
        return enrollmentBank;
    }

    public void setEnrollmentBank(String enrollmentBank) {
        this.enrollmentBank = enrollmentBank;
    }

    public String getEnrollmentBranch() {
        return enrollmentBranch;
    }

    public void setEnrollmentBranch(String enrollmentBranch) {
        this.enrollmentBranch = enrollmentBranch;
    }

    public boolean isAccountOpened() {
        return accountOpened;
    }

    public void setAccountOpened(boolean accountOpened) {
        this.accountOpened = accountOpened;
    }

    public boolean isPinSet() {
        return pinSet;
    }

    public void setPinSet(boolean pinSet) {
        this.pinSet = pinSet;
    }

    public boolean isPinActive() {
        return pinActive;
    }

    public void setPinActive(boolean pinActive) {
        this.pinActive = pinActive;
    }

}

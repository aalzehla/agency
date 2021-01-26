/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class ValidateAccountByNibssBvnResponse extends BaseResponse {

    @JsonProperty("BVN")
    private String bvn;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("DateOfBirth")
    private String dob;

    @JsonProperty("RegistrationDate")
    private String registrationDate;

    @JsonProperty("EnrollmentBank")
    private String enrollmentBank;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("LevelOfAccount")
    private String levelOfAccount;

    @JsonProperty("LgaOfOrigin")
    private String lgaOfOrigin;

    @JsonProperty("LgaOfResidence")
    private Object lgaOfResidence;

    @JsonProperty("MaritalStatus")
    private String maritalStatus;

    @JsonProperty("NIN")
    private String nin;

    @JsonProperty("NameOnCard")
    private String nameOnCard;

    @JsonProperty("Nationality")
    private String nationality;

    @JsonProperty("PhoneNumber1")
    private String phoneNumber1;

    @JsonProperty("PhoneNumber2")
    private String phoneNumber2;

    @JsonProperty("ResidentialAddress")
    private String residentialAddress;

    @JsonProperty("StateOfOrigin")
    private String stateOfOrigin;

    @JsonProperty("StateOfResidence")
    private String stateOfResidence;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("EnrollmentBranch")
    private String enrollmentBranch;

    @JsonProperty("WatchListed")
    private String watchListed;

    @JsonProperty("Base64Image")
    private String base64Image;

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEnrollmentBank() {
        return enrollmentBank;
    }

    public void setEnrollmentBank(String enrollmentBank) {
        this.enrollmentBank = enrollmentBank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLevelOfAccount() {
        return levelOfAccount;
    }

    public void setLevelOfAccount(String levelOfAccount) {
        this.levelOfAccount = levelOfAccount;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public Object getLgaOfResidence() {
        return lgaOfResidence;
    }

    public void setLgaOfResidence(Object lgaOfResidence) {
        this.lgaOfResidence = lgaOfResidence;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getStateOfResidence() {
        return stateOfResidence;
    }

    public void setStateOfResidence(String stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnrollmentBranch() {
        return enrollmentBranch;
    }

    public void setEnrollmentBranch(String enrollmentBranch) {
        this.enrollmentBranch = enrollmentBranch;
    }

    public String getWatchListed() {
        return watchListed;
    }

    public void setWatchListed(String watchListed) {
        this.watchListed = watchListed;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}

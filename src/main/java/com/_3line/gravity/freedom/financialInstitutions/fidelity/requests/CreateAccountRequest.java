package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class CreateAccountRequest {

    @JsonProperty("Email")
    private String email;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("HouseNo")
    private String houseNo;
    @JsonProperty("Nationality")
    private String nationality;
    @JsonProperty("PhoneNo")
    private String phoneNo;
    @JsonProperty("StreetName")
    private String streetName;
    @JsonProperty("Surname")
    private String surname;
    @JsonProperty("BusStop")
    private String busStop;
    @JsonProperty("MaritalStatus")
    private String maritalStatus;
    @JsonProperty("MotherMaidenName")
    private String motherMaidenName;
    @JsonProperty("PermanentAddress")
    private String permanentAddress;
    @JsonProperty("State")
    private String state;
    @JsonProperty("StateOfOrigin")
    private String stateOfOrigin;
    @JsonProperty("Occupation")
    private String occupation;
    @JsonProperty("OtherName")
    private String otherName;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("Photo")
    private byte[] photo;
    @JsonProperty("Signature")
    private byte[] signature;
    @JsonProperty("BankVerificationNumber")
    private String bankVerificationNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBusStop() {
        return busStop;
    }

    public void setBusStop(String busStop) {
        this.busStop = busStop;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getBankVerificationNumber() {
        return bankVerificationNumber;
    }

    public void setBankVerificationNumber(String bankVerificationNumber) {
        this.bankVerificationNumber = bankVerificationNumber;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CreateAccountRequest{");
        sb.append("Email='").append(email).append('\'');
        sb.append(", FirstName='").append(firstName).append('\'');
        sb.append(", HouseNo='").append(houseNo).append('\'');
        sb.append(", PhoneNo='").append(phoneNo).append('\'');
        sb.append(", Surname='").append(surname).append('\'');
        sb.append(", MaritalStatus='").append(maritalStatus).append('\'');
        sb.append(", PermanentAddress='").append(permanentAddress).append('\'');
        sb.append(", State='").append(state).append('\'');
        sb.append(", StateOfOrigin='").append(stateOfOrigin).append('\'');
        sb.append(", Occupation='").append(occupation).append('\'');
        sb.append(", OtherName='").append(otherName).append('\'');
        sb.append(", Gender='").append(gender).append('\'');
        sb.append(", DateOfBirth=").append(dateOfBirth);
        sb.append(", Nationality='").append(nationality).append('\'');
        sb.append(", StreetName='").append(streetName).append('\'');
        sb.append(", BusStop='").append(busStop).append('\'');
        sb.append(", MotherMaidenName='").append(motherMaidenName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

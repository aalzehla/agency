package com._3line.gravity.api.account.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class FidelityAccountOpeningDTO {

    @Min(value = 1, message = "First Name cannot be empty")
    private String firstName;
    @Min(value = 1, message = "Last Name cannot be empty")
    private String lastName;
    private String middleName;
    private String title;
    private String dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String address;
    @Pattern(regexp = "[0-9]{11,11}",message = "Phone number should be a valid number of length 11")
    private String phoneNo;
    private String lga;
    private String state;
    private String country;
    private String bvn;
    @Pattern(regexp = "^(?:(?=.{0,50}$)[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+){1}@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+‌​‌​)*(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z‌​]{2,}))?$",
            message = "Enter valid email eg. agent.account@email.com")
    private String email;
    private String occupation;
    private String customerMandate;

}

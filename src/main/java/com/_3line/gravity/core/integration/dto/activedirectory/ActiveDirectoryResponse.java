package com._3line.gravity.core.integration.dto.activedirectory;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ActiveDirectoryResponse {

	@JsonProperty("managerDepartment")
	private Object managerDepartment;

	@JsonProperty("requestNumber")
	private RequestNumber requestNumber;

	@JsonProperty("responseDescription")
	private String responseDescription;

	@JsonProperty("displayName")
	private Object displayName;

	@JsonProperty("staffName")
	private Object staffName;

	@JsonProperty("groups")
	private Object groups;

	@JsonProperty("mobileNo")
	private Object mobileNo;

	@JsonProperty("department")
	private Object department;

	@JsonProperty("managerName")
	private Object managerName;

	@JsonProperty("email")
	private Object email;

	@JsonProperty("staffID")
	private Object staffID;

	@JsonProperty("responseCode")
	private String responseCode;

	public void setManagerDepartment(Object managerDepartment){
		this.managerDepartment = managerDepartment;
	}

	public Object getManagerDepartment(){
		return managerDepartment;
	}

	public RequestNumber getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(RequestNumber requestNumber) {
		this.requestNumber = requestNumber;
	}

	public void setResponseDescription(String responseDescription){
		this.responseDescription = responseDescription;
	}

	public String getResponseDescription(){
		return responseDescription;
	}

	public void setDisplayName(Object displayName){
		this.displayName = displayName;
	}

	public Object getDisplayName(){
		return displayName;
	}

	public void setStaffName(Object staffName){
		this.staffName = staffName;
	}

	public Object getStaffName(){
		return staffName;
	}

	public void setGroups(Object groups){
		this.groups = groups;
	}

	public Object getGroups(){
		return groups;
	}

	public void setMobileNo(Object mobileNo){
		this.mobileNo = mobileNo;
	}

	public Object getMobileNo(){
		return mobileNo;
	}

	public void setDepartment(Object department){
		this.department = department;
	}

	public Object getDepartment(){
		return department;
	}

	public void setManagerName(Object managerName){
		this.managerName = managerName;
	}

	public Object getManagerName(){
		return managerName;
	}

	public void setEmail(Object email){
		this.email = email;
	}

	public Object getEmail(){
		return email;
	}

	public void setStaffID(Object staffID){
		this.staffID = staffID;
	}

	public Object getStaffID(){
		return staffID;
	}

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"managerDepartment = '" + managerDepartment + '\'' + 
			",requestNumber = '" + requestNumber + '\'' + 
			",responseDescription = '" + responseDescription + '\'' + 
			",displayName = '" + displayName + '\'' + 
			",staffName = '" + staffName + '\'' + 
			",groups = '" + groups + '\'' + 
			",mobileNo = '" + mobileNo + '\'' + 
			",department = '" + department + '\'' + 
			",managerName = '" + managerName + '\'' + 
			",email = '" + email + '\'' + 
			",staffID = '" + staffID + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}
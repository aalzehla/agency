package com._3line.gravity.core.integration.dto.activedirectory;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ActiveDirectoryRequest {

	@JsonProperty("password")
	private String password;

	@JsonProperty("requestNumber")
	private String requestNumber;

	@JsonProperty("staffId")
	private String staffId;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setRequestNumber(String requestNumber){
		this.requestNumber = requestNumber;
	}

	public String getRequestNumber(){
		return requestNumber;
	}

	public void setStaffId(String staffId){
		this.staffId = staffId;
	}

	public String getStaffId(){
		return staffId;
	}

	@Override
 	public String toString(){
		return 
			"Request{" + 
			"password = '" + password + '\'' + 
			",requestNumber = '" + requestNumber + '\'' + 
			",staffId = '" + staffId + '\'' + 
			"}";
		}
}
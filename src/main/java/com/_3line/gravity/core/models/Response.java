/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Response {

    private String respCode;
    private String respDescription;

    @JsonProperty("respBody")
    private Object respBody;

    public Response(){

    }

    public Response(String respCode,String respDescription,Object respBody){
        this.respCode = respCode;
        this.respDescription = respDescription;
        this.respBody = respBody;
    }


}

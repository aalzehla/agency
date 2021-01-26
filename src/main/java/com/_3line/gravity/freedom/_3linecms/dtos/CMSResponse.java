package com._3line.gravity.freedom._3linecms.dtos;


import lombok.ToString;

@ToString
public class CMSResponse {

    private String respcode ;
    private String respdesc;


    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getRespdesc() {
        return respdesc;
    }

    public void setRespdesc(String respdesc) {
        this.respdesc = respdesc;
    }
}

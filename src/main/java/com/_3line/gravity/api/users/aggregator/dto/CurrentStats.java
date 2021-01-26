/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.api.users.aggregator.dto;


import lombok.Data;

@Data
public class CurrentStats {

    private String totals;
    private String totalCount;
    private String deposits;
    private String depositCount;
    private String withDrawals;
    private String withDrawalsCount;
    private String billPayments;
    private String billPaymentsCount;

}

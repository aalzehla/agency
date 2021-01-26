package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.utility.Utility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.Date;


@Entity
public class NIBBSReconcilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount ;

    private String remark ;

    private String terminalid ;


}

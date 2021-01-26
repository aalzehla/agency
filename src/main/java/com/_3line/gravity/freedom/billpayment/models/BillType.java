package com._3line.gravity.freedom.billpayment.models;


import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Labi
 * @date 29/06/2019
 */
@Data
@Entity
public class BillType extends AbstractEntity {

    private String categoryName;
    private String categoryCode;
    private String description;

//    @OneToMany
//    private List<BillServices> services;

}

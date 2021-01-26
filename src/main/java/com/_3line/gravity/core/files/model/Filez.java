package com._3line.gravity.core.files.model;

import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Filez extends AbstractEntity {

    private String purpose ;
    private String uploadedBy;
    private String location ;
    private String name ;
    private String originalName ;
    private String size ;
    private String extension ;
    private String contentType ;
    private String url ;

}

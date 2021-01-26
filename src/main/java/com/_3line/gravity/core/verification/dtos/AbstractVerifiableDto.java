package com._3line.gravity.core.verification.dtos;

public abstract class AbstractVerifiableDto {

    private Long id ;
    private int version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

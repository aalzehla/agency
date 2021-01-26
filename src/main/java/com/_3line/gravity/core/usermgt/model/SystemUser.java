package com._3line.gravity.core.usermgt.model;

import com._3line.gravity.core.code.model.Code;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Set;

@ToString
@Entity(name = "garvity_system_user")
@Where(clause ="del_Flag='N'" )
public class SystemUser extends AbstractUser {

    @ManyToOne
    private Code branch;

    private String lastIpAddress;


    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public Code getBranch() {
        return branch;
    }

    public void setBranch(Code branch) {
        this.branch = branch;
    }

}

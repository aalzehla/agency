package com._3line.gravity.core.code.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import java.util.Arrays;
import java.util.List;


/**
 *
 * The {@code Code} class model represents unique data that can be used for system configurations.
 * This can be used to set up a list of items that can be presented in drop-down menus
 * Example is a country eg Nigeria with code: NG, type: COUNTRY and description: the description if necessary
 * With this approach, new items can be added or removed from a list presented to the user
 * @author FortunatusE
 * @date 11/6/2018
 */

@Entity
@Table(name="code", uniqueConstraints= @UniqueConstraint(columnNames={"code", "type"}))
@Where(clause ="del_Flag='N'" )
public class Code extends AbstractEntity {

     private String code;
        private String type;
        private String description;
        private String extraInfo;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getExtraInfo() {
            return extraInfo;
        }

        public void setExtraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
        }



    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("code", "type");
    }


        @Override
        public String toString() {
            return "Code{" +
                    "code='" + code + '\'' +
                    ", type='" + type + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }



}

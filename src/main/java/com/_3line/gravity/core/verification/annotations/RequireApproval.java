package com._3line.gravity.core.verification.annotations;


import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.core.operation.annotation.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation
public @interface RequireApproval {

    @AliasFor(annotation = Operation.class)
    String code() ;


    /**
     * Class type of the persister for the entity
     * @return
     */
    Class entityType ()  ;
}

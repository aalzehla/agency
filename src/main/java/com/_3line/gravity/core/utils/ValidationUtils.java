package com._3line.gravity.core.utils;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author FortunatusE
 * @date 11/22/2018
 */
public class ValidationUtils {


    private static Validator validator;


    private static Validator getValidator(){

        if(validator == null){
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    public static String validateObject(Object object){

        String errorMessage = "";

        Set<ConstraintViolation<Object>> violations = getValidator().validate(object);

        if(!violations.isEmpty()){

            StringBuilder errorMessageBuilder = new StringBuilder();

            for(ConstraintViolation<Object> violation: violations){
                errorMessageBuilder.append(violation.getMessage()+"\n");
            }
            errorMessage = errorMessageBuilder.toString();
        }
        return errorMessage;
    }

}

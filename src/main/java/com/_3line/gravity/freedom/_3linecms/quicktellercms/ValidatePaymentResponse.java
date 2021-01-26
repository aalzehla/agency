
package com._3line.gravity.freedom._3linecms.quicktellercms;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="validatePaymentReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "validatePaymentReturn"
})
@XmlRootElement(name = "validatePaymentResponse")
public class ValidatePaymentResponse {

    @XmlElement(required = true, nillable = true)
    protected String validatePaymentReturn;

    /**
     * Gets the value of the validatePaymentReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidatePaymentReturn() {
        return validatePaymentReturn;
    }

    /**
     * Sets the value of the validatePaymentReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidatePaymentReturn(String value) {
        this.validatePaymentReturn = value;
    }

}

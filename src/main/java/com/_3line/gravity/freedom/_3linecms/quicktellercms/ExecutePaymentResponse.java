
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
 *         &lt;element name="executePaymentReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "executePaymentReturn"
})
@XmlRootElement(name = "executePaymentResponse")
public class ExecutePaymentResponse {

    @XmlElement(required = true, nillable = true)
    protected String executePaymentReturn;

    /**
     * Gets the value of the executePaymentReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExecutePaymentReturn() {
        return executePaymentReturn;
    }

    /**
     * Sets the value of the executePaymentReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExecutePaymentReturn(String value) {
        this.executePaymentReturn = value;
    }

}

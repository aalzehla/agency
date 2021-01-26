
package com._3line.gravity.freedom._3linecms.saopservice;

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
 *         &lt;element name="sendDataTo3LineReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "sendDataTo3LineReturn"
})
@XmlRootElement(name = "sendDataTo3LineResponse")
public class SendDataTo3LineResponse {

    @XmlElement(required = true, nillable = true)
    protected String sendDataTo3LineReturn;

    /**
     * Gets the value of the sendDataTo3LineReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendDataTo3LineReturn() {
        return sendDataTo3LineReturn;
    }

    /**
     * Sets the value of the sendDataTo3LineReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendDataTo3LineReturn(String value) {
        this.sendDataTo3LineReturn = value;
    }

}

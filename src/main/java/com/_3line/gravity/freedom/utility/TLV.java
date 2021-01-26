/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author OlalekanW
 */
public class TLV {

    Map<String, String> tlv = new HashMap<String, String>();

    public TLV() {
    }

    public TLV(String tlv) throws Exception {
        if (tlv == null || tlv.length() < 7) {
            throw new Exception("Swipe TLV format : length is < 7 in :" + tlv);
        }
        if (!tlv.startsWith("SWP")) {
            throw new Exception("Swipe TLV format : missing SWP in :" + tlv);
        }
        try {
            int totallength = Integer.parseInt(tlv.substring(3, 7));
            if (totallength != tlv.substring(7).length()) {
                throw new Exception("Swipe TLV format : inconsistent tlv data lenth for tag SWP expecting data value of lengh : " + totallength + " in :" + tlv);
            }

        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid legth field  for TAG SWP - " + tlv.substring(3, 7) + " in :" + tlv);
        }

        int length = 0, index = -1;
        try {

            if ((index = tlv.indexOf("SIG")) != -1 && (length = Integer.parseInt(tlv.substring(index + 3, index + 7))) > 0) {
                this.tlv.put("SIG", tlv.substring(index + 7, index + 7 + length));
            } else {
                throw new Exception("Swipe TLV format :  unfound Tag SIG in " + tlv);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid length field for tag SIG - " + tlv.substring(index + 3, index + 7) + " in :" + tlv);
        } catch (StringIndexOutOfBoundsException e) {
            throw new Exception("Swipe TLV format : Inconsistent data for tag SIG in " + tlv);
        }

        length = 0;
        index = -1;
        try {

            if ((index = tlv.indexOf("MID")) != -1 && (length = Integer.parseInt(tlv.substring(index + 3, index + 7))) > 0) {
                this.tlv.put("MID", tlv.substring(index + 7, index + 7 + length));
            } else {
                throw new Exception("Swipe TLV format :  unfound Tag MID in " + tlv);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid length field for tag MID - " + tlv.substring(index + 3, index + 7) + " in :" + tlv);
        } catch (StringIndexOutOfBoundsException e) {
            throw new Exception("Swipe TLV format : Inconsistent data for tag MID in " + tlv);
        }

        length = 0;
        index = -1;
        try {

            if ((index = tlv.indexOf("AID")) != -1 && (length = Integer.parseInt(tlv.substring(index + 3, index + 7))) > 0) {
                this.tlv.put("AID", tlv.substring(index + 7, index + 7 + length));
            } else {
                throw new Exception("Swipe TLV format :  unfound Tag AID in " + tlv);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid length field for tag AID - " + tlv.substring(index + 3, index + 7) + " in :" + tlv);
        } catch (StringIndexOutOfBoundsException e) {
            throw new Exception("Swipe TLV format : Inconsistent data for tag AID in " + tlv);
        }

        length = 0;
        index = -1;
        try {

            if ((index = tlv.indexOf("CMN")) != -1 && (length = Integer.parseInt(tlv.substring(index + 3, index + 7))) > 0) {
                this.tlv.put("CMN", tlv.substring(index + 7, index + 7 + length));
            } else {
                throw new Exception("Swipe TLV format :  unfound Tag CMN in " + tlv);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid length field for tag CMN - " + tlv.substring(index + 3, index + 7) + " in :" + tlv);
        } catch (StringIndexOutOfBoundsException e) {
            throw new Exception("Swipe TLV format : Inconsistent data for tag CMN in " + tlv);
        }

        length = 0;
        index = -1;
        try {

            if ((index = tlv.indexOf("CEM")) != -1 && (length = Integer.parseInt(tlv.substring(index + 3, index + 7))) > 0) {
                this.tlv.put("CEM", tlv.substring(index + 7, index + 7 + length));
            } else {
                throw new Exception("Swipe TLV format :  unfound Tag CEM in " + tlv);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid length field for tag CEM - " + tlv.substring(index + 3, index + 7) + " in :" + tlv);
        } catch (StringIndexOutOfBoundsException e) {
            throw new Exception("Swipe TLV format : Inconsistent data for tag CEM in " + tlv);
        }

        length = 0;
        index = -1;
        try {

            if ((index = tlv.indexOf("AMT")) != -1 && (length = Integer.parseInt(tlv.substring(index + 3, index + 7))) > 0) {
                String amt = tlv.substring(index + 7, index + 7 + length);
                try {
                    Double.parseDouble(amt);
                } catch (NumberFormatException e) {
                    throw new Exception("Swipe TLV format : invalid amount for AMT tag - " + amt + " in :" + tlv);
                }
                this.tlv.put("AMT", amt);
            } else {
                throw new Exception("Swipe TLV format :  unfound Tag AMT in " + tlv);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Swipe TLV format : invalid length field for tag AMT - " + tlv.substring(index + 3, index + 7) + " in :" + tlv);
        } catch (StringIndexOutOfBoundsException e) {
            throw new Exception("Swipe TLV format : Inconsistent data for tag AMT in " + tlv);
        }

    }

    public String getTag(String tag) {
        String value = tlv.get(tag);
        if (value == null) {
            return "";
        }
        return value;
    }

    public void setTag(String tag, String value) {
        tlv.put(tag, value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int totallength = 0;
        for (Map.Entry<String, String> entry : tlv.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key);
            sb.append(String.format("%04d", value.length()));
            sb.append(value);
            totallength += 3 + 4 + value.length();
        }
        return "SWP" + String.format("%04d", totallength) + sb.toString();

    }

}

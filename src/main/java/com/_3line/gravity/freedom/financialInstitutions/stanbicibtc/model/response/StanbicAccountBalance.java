package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StanbicAccountBalance  {

        @JsonProperty
        private String acct_number;

        @JsonProperty
        private String acct_name;

        @JsonProperty
        private String acct_crncy;

        @JsonProperty
        private String available_balance;

        @JsonProperty
        private String lien_amt;

        @JsonProperty
        private String unclr_balance;

        @JsonProperty
        private String od_balance;

        public String getAcct_number() {
            return acct_number;
        }

        public void setAcct_number(String acct_number) {
            this.acct_number = acct_number;
        }

        public String getAcct_name() {
            return acct_name;
        }

        public void setAcct_name(String acct_name) {
            this.acct_name = acct_name;
        }

        public String getAcct_crncy() {
            return acct_crncy;
        }

        public void setAcct_crncy(String acct_crncy) {
            this.acct_crncy = acct_crncy;
        }

        public String getAvailable_balance() {
            return available_balance;
        }

        public void setAvailable_balance(String available_balance) {
            this.available_balance = available_balance;
        }

        public String getLien_amt() {
            return lien_amt;
        }

        public void setLien_amt(String lien_amt) {
            this.lien_amt = lien_amt;
        }

        public String getUnclr_balance() {
            return unclr_balance;
        }

        public void setUnclr_balance(String unclr_balance) {
            this.unclr_balance = unclr_balance;
        }

        public String getOd_balance() {
            return od_balance;
        }

        public void setOd_balance(String od_balance) {
            this.od_balance = od_balance;
        }

}

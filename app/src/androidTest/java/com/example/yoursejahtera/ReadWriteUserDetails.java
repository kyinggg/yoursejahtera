package com.example.yoursejahtera;

public class ReadWriteUserDetails {
    public String ic, address, gender, mobile;

    public ReadWriteUserDetails(String textIc, String textAddress, String textGender,String textMobile) {
        this.ic = textIc;
        this.address = textAddress;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}

package com.tbs.app.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class WithdrawalRequest {

    public WithdrawalRequest() {
    }

    public WithdrawalRequest(double amount, String otp, String remark) {
        this.amount = amount;
        this.otp = otp;
        this.remark = remark;
    }
    
    
    //set Min withdrawal USD 10
    @Min(10)
    private double amount;

    @NotBlank
    @Size(max = 6, message = "otp must not blank, fill with 6 digit code from SMS your phone")
    private String otp;

    @NotBlank(message = "remark must not blank, fill with string merchant_id|terminal_id")
    private String remark;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    
}

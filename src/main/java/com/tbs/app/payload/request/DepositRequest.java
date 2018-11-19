package com.tbs.app.payload.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class DepositRequest {

    public DepositRequest() {
    }

    public DepositRequest(double amount, String remark) {
        this.amount = amount;
        this.remark = remark;
    }

    //set Min deposit USD 10
    @Min(10)
    private double amount;

    @NotBlank(message = "remark must not blank, fill with string merchant_id|terminal_id")
    private String remark;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

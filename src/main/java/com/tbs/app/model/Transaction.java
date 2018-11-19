package com.tbs.app.model;

import com.tbs.app.model.enums.TrxSide;
import com.tbs.app.model.audit.DateAudit;
import com.tbs.app.model.enums.TrxType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "transaction")
public class Transaction extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TrxSide side;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TrxType type;

    @Column(name = "amount", columnDefinition = "Decimal(10,2) default '00.00'")
    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "trx_date")
    private Date trxDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    private String remark;

    public Long getId() {
        return id;
    }

    public TrxSide getSide() {
        return side;
    }

    public void setSide(TrxSide side) {
        this.side = side;
    }

    public TrxType getType() {
        return type;
    }

    public void setType(TrxType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(Date trxDate) {
        this.trxDate = trxDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

package in.ravikalla.microservices.savingsaccount.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Savingsaccount.
 */
@Entity
@Table(name = "savingsaccount")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Savingsaccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "accountnumber", nullable = false)
    private Integer accountnumber;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "accountname", length = 100, nullable = false)
    private String accountname;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "generalinfo")
    private String generalinfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccountnumber() {
        return accountnumber;
    }

    public Savingsaccount accountnumber(Integer accountnumber) {
        this.accountnumber = accountnumber;
        return this;
    }

    public void setAccountnumber(Integer accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getAccountname() {
        return accountname;
    }

    public Savingsaccount accountname(String accountname) {
        this.accountname = accountname;
        return this;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public Double getAmount() {
        return amount;
    }

    public Savingsaccount amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getGeneralinfo() {
        return generalinfo;
    }

    public Savingsaccount generalinfo(String generalinfo) {
        this.generalinfo = generalinfo;
        return this;
    }

    public void setGeneralinfo(String generalinfo) {
        this.generalinfo = generalinfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Savingsaccount savingsaccount = (Savingsaccount) o;
        if (savingsaccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), savingsaccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Savingsaccount{" +
            "id=" + getId() +
            ", accountnumber=" + getAccountnumber() +
            ", accountname='" + getAccountname() + "'" +
            ", amount=" + getAmount() +
            ", generalinfo='" + getGeneralinfo() + "'" +
            "}";
    }
}

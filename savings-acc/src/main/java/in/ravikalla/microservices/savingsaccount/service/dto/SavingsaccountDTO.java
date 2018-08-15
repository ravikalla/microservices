package in.ravikalla.microservices.savingsaccount.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Savingsaccount entity.
 */
public class SavingsaccountDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer accountnumber;

    @NotNull
    @Size(min = 1, max = 100)
    private String accountname;

    @NotNull
    private Double amount;

    private String generalinfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(Integer accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getGeneralinfo() {
        return generalinfo;
    }

    public void setGeneralinfo(String generalinfo) {
        this.generalinfo = generalinfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SavingsaccountDTO savingsaccountDTO = (SavingsaccountDTO) o;
        if (savingsaccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), savingsaccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SavingsaccountDTO{" +
            "id=" + getId() +
            ", accountnumber=" + getAccountnumber() +
            ", accountname='" + getAccountname() + "'" +
            ", amount=" + getAmount() +
            ", generalinfo='" + getGeneralinfo() + "'" +
            "}";
    }
}

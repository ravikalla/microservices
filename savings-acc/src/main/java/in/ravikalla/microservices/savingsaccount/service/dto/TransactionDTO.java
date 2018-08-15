package in.ravikalla.microservices.savingsaccount.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Transaction entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "-100000000000")
    @DecimalMax(value = "100000000000")
    private Double amount;

    @NotNull
    private LocalDate transactiondate;

    private Long savingsaccountId;

    private String savingsaccountAccountnumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(LocalDate transactiondate) {
        this.transactiondate = transactiondate;
    }

    public Long getSavingsaccountId() {
        return savingsaccountId;
    }

    public void setSavingsaccountId(Long savingsaccountId) {
        this.savingsaccountId = savingsaccountId;
    }

    public String getSavingsaccountAccountnumber() {
        return savingsaccountAccountnumber;
    }

    public void setSavingsaccountAccountnumber(String savingsaccountAccountnumber) {
        this.savingsaccountAccountnumber = savingsaccountAccountnumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (transactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", transactiondate='" + getTransactiondate() + "'" +
            ", savingsaccount=" + getSavingsaccountId() +
            ", savingsaccount='" + getSavingsaccountAccountnumber() + "'" +
            "}";
    }
}

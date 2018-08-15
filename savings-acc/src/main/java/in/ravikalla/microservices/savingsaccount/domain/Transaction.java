package in.ravikalla.microservices.savingsaccount.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Transaction.
 */
@Entity
@Table(name = "jhi_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @DecimalMin(value = "-100000000000")
    @DecimalMax(value = "100000000000")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "transactiondate", nullable = false)
    private LocalDate transactiondate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Savingsaccount savingsaccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getTransactiondate() {
        return transactiondate;
    }

    public Transaction transactiondate(LocalDate transactiondate) {
        this.transactiondate = transactiondate;
        return this;
    }

    public void setTransactiondate(LocalDate transactiondate) {
        this.transactiondate = transactiondate;
    }

    public Savingsaccount getSavingsaccount() {
        return savingsaccount;
    }

    public Transaction savingsaccount(Savingsaccount savingsaccount) {
        this.savingsaccount = savingsaccount;
        return this;
    }

    public void setSavingsaccount(Savingsaccount savingsaccount) {
        this.savingsaccount = savingsaccount;
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
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", transactiondate='" + getTransactiondate() + "'" +
            "}";
    }
}

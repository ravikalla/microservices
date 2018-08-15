package in.ravikalla.microservices.savingsaccount.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Transaction entity. This class is used in TransactionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /transactions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private DoubleFilter amount;

    private LocalDateFilter transactiondate;

    private LongFilter savingsaccountId;

    public TransactionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public LocalDateFilter getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(LocalDateFilter transactiondate) {
        this.transactiondate = transactiondate;
    }

    public LongFilter getSavingsaccountId() {
        return savingsaccountId;
    }

    public void setSavingsaccountId(LongFilter savingsaccountId) {
        this.savingsaccountId = savingsaccountId;
    }

    @Override
    public String toString() {
        return "TransactionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (transactiondate != null ? "transactiondate=" + transactiondate + ", " : "") +
                (savingsaccountId != null ? "savingsaccountId=" + savingsaccountId + ", " : "") +
            "}";
    }

}

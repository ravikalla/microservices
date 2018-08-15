package in.ravikalla.microservices.savingsaccount.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Savingsaccount entity. This class is used in SavingsaccountResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /savingsaccounts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SavingsaccountCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter accountnumber;

    private StringFilter accountname;

    private DoubleFilter amount;

    private StringFilter generalinfo;

    public SavingsaccountCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(IntegerFilter accountnumber) {
        this.accountnumber = accountnumber;
    }

    public StringFilter getAccountname() {
        return accountname;
    }

    public void setAccountname(StringFilter accountname) {
        this.accountname = accountname;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public StringFilter getGeneralinfo() {
        return generalinfo;
    }

    public void setGeneralinfo(StringFilter generalinfo) {
        this.generalinfo = generalinfo;
    }

    @Override
    public String toString() {
        return "SavingsaccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (accountnumber != null ? "accountnumber=" + accountnumber + ", " : "") +
                (accountname != null ? "accountname=" + accountname + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (generalinfo != null ? "generalinfo=" + generalinfo + ", " : "") +
            "}";
    }

}

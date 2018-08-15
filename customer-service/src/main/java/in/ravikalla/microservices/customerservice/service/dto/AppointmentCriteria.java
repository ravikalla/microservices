package in.ravikalla.microservices.customerservice.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Appointment entity. This class is used in AppointmentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /appointments?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppointmentCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter visitorname;

    private ZonedDateTimeFilter time;

    private StringFilter comments;

    public AppointmentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVisitorname() {
        return visitorname;
    }

    public void setVisitorname(StringFilter visitorname) {
        this.visitorname = visitorname;
    }

    public ZonedDateTimeFilter getTime() {
        return time;
    }

    public void setTime(ZonedDateTimeFilter time) {
        this.time = time;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "AppointmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (visitorname != null ? "visitorname=" + visitorname + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
            "}";
    }

}

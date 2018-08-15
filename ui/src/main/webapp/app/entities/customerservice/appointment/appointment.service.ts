import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAppointment } from 'app/shared/model/customerservice/appointment.model';

type EntityResponseType = HttpResponse<IAppointment>;
type EntityArrayResponseType = HttpResponse<IAppointment[]>;

@Injectable({ providedIn: 'root' })
export class AppointmentService {
    private resourceUrl = SERVER_API_URL + 'customerservice/api/appointments';

    constructor(private http: HttpClient) {}

    create(appointment: IAppointment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(appointment);
        return this.http
            .post<IAppointment>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    update(appointment: IAppointment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(appointment);
        return this.http
            .put<IAppointment>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAppointment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAppointment[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(appointment: IAppointment): IAppointment {
        const copy: IAppointment = Object.assign({}, appointment, {
            time: appointment.time != null && appointment.time.isValid() ? appointment.time.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.time = res.body.time != null ? moment(res.body.time) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((appointment: IAppointment) => {
            appointment.time = appointment.time != null ? moment(appointment.time) : null;
        });
        return res;
    }
}

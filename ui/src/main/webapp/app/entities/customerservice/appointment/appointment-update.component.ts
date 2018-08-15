import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAppointment } from 'app/shared/model/customerservice/appointment.model';
import { AppointmentService } from './appointment.service';

@Component({
    selector: 'jhi-appointment-update',
    templateUrl: './appointment-update.component.html'
})
export class AppointmentUpdateComponent implements OnInit {
    private _appointment: IAppointment;
    isSaving: boolean;
    time: string;

    constructor(private appointmentService: AppointmentService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ appointment }) => {
            this.appointment = appointment;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.appointment.time = moment(this.time, DATE_TIME_FORMAT);
        if (this.appointment.id !== undefined) {
            this.subscribeToSaveResponse(this.appointmentService.update(this.appointment));
        } else {
            this.subscribeToSaveResponse(this.appointmentService.create(this.appointment));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>) {
        result.subscribe((res: HttpResponse<IAppointment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get appointment() {
        return this._appointment;
    }

    set appointment(appointment: IAppointment) {
        this._appointment = appointment;
        this.time = moment(appointment.time).format(DATE_TIME_FORMAT);
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppointment } from 'app/shared/model/customerservice/appointment.model';

@Component({
    selector: 'jhi-appointment-detail',
    templateUrl: './appointment-detail.component.html'
})
export class AppointmentDetailComponent implements OnInit {
    appointment: IAppointment;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ appointment }) => {
            this.appointment = appointment;
        });
    }

    previousState() {
        window.history.back();
    }
}

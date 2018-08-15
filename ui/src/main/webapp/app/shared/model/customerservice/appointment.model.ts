import { Moment } from 'moment';

export interface IAppointment {
    id?: number;
    visitorname?: string;
    time?: Moment;
    comments?: string;
}

export class Appointment implements IAppointment {
    constructor(public id?: number, public visitorname?: string, public time?: Moment, public comments?: string) {}
}

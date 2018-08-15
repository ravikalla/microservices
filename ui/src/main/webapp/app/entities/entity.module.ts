import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { UiSavingsaccountModule as SavingsaccountSavingsaccountModule } from './savingsaccount/savingsaccount/savingsaccount.module';
import { UiTransactionModule as SavingsaccountTransactionModule } from './savingsaccount/transaction/transaction.module';
import { UiAppointmentModule as CustomerserviceAppointmentModule } from './customerservice/appointment/appointment.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        SavingsaccountSavingsaccountModule,
        SavingsaccountTransactionModule,
        CustomerserviceAppointmentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UiEntityModule {}

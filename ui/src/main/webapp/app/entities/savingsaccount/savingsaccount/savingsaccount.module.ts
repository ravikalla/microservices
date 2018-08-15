import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UiSharedModule } from 'app/shared';
import {
    SavingsaccountComponent,
    SavingsaccountDetailComponent,
    SavingsaccountUpdateComponent,
    SavingsaccountDeletePopupComponent,
    SavingsaccountDeleteDialogComponent,
    savingsaccountRoute,
    savingsaccountPopupRoute
} from './';

const ENTITY_STATES = [...savingsaccountRoute, ...savingsaccountPopupRoute];

@NgModule({
    imports: [UiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SavingsaccountComponent,
        SavingsaccountDetailComponent,
        SavingsaccountUpdateComponent,
        SavingsaccountDeleteDialogComponent,
        SavingsaccountDeletePopupComponent
    ],
    entryComponents: [
        SavingsaccountComponent,
        SavingsaccountUpdateComponent,
        SavingsaccountDeleteDialogComponent,
        SavingsaccountDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UiSavingsaccountModule {}

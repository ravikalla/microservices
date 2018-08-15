import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UiSharedModule } from 'app/shared';
import {
    TransactionComponent,
    TransactionDetailComponent,
    TransactionUpdateComponent,
    TransactionDeletePopupComponent,
    TransactionDeleteDialogComponent,
    transactionRoute,
    transactionPopupRoute
} from './';

const ENTITY_STATES = [...transactionRoute, ...transactionPopupRoute];

@NgModule({
    imports: [UiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TransactionComponent,
        TransactionDetailComponent,
        TransactionUpdateComponent,
        TransactionDeleteDialogComponent,
        TransactionDeletePopupComponent
    ],
    entryComponents: [TransactionComponent, TransactionUpdateComponent, TransactionDeleteDialogComponent, TransactionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UiTransactionModule {}

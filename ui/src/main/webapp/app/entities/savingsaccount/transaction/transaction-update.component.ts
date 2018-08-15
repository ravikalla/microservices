import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITransaction } from 'app/shared/model/savingsaccount/transaction.model';
import { TransactionService } from './transaction.service';
import { ISavingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';
import { SavingsaccountService } from 'app/entities/savingsaccount/savingsaccount';

@Component({
    selector: 'jhi-transaction-update',
    templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
    private _transaction: ITransaction;
    isSaving: boolean;

    savingsaccounts: ISavingsaccount[];
    transactiondateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private transactionService: TransactionService,
        private savingsaccountService: SavingsaccountService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ transaction }) => {
            this.transaction = transaction;
        });
        this.savingsaccountService.query().subscribe(
            (res: HttpResponse<ISavingsaccount[]>) => {
                this.savingsaccounts = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.transaction.id !== undefined) {
            this.subscribeToSaveResponse(this.transactionService.update(this.transaction));
        } else {
            this.subscribeToSaveResponse(this.transactionService.create(this.transaction));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>) {
        result.subscribe((res: HttpResponse<ITransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSavingsaccountById(index: number, item: ISavingsaccount) {
        return item.id;
    }
    get transaction() {
        return this._transaction;
    }

    set transaction(transaction: ITransaction) {
        this._transaction = transaction;
    }
}

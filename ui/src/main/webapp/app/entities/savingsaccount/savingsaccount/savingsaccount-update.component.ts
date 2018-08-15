import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISavingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';
import { SavingsaccountService } from './savingsaccount.service';

@Component({
    selector: 'jhi-savingsaccount-update',
    templateUrl: './savingsaccount-update.component.html'
})
export class SavingsaccountUpdateComponent implements OnInit {
    private _savingsaccount: ISavingsaccount;
    isSaving: boolean;

    constructor(private savingsaccountService: SavingsaccountService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ savingsaccount }) => {
            this.savingsaccount = savingsaccount;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.savingsaccount.id !== undefined) {
            this.subscribeToSaveResponse(this.savingsaccountService.update(this.savingsaccount));
        } else {
            this.subscribeToSaveResponse(this.savingsaccountService.create(this.savingsaccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISavingsaccount>>) {
        result.subscribe((res: HttpResponse<ISavingsaccount>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get savingsaccount() {
        return this._savingsaccount;
    }

    set savingsaccount(savingsaccount: ISavingsaccount) {
        this._savingsaccount = savingsaccount;
    }
}

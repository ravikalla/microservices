import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISavingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';

@Component({
    selector: 'jhi-savingsaccount-detail',
    templateUrl: './savingsaccount-detail.component.html'
})
export class SavingsaccountDetailComponent implements OnInit {
    savingsaccount: ISavingsaccount;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ savingsaccount }) => {
            this.savingsaccount = savingsaccount;
        });
    }

    previousState() {
        window.history.back();
    }
}

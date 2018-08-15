import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Savingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';
import { SavingsaccountService } from './savingsaccount.service';
import { SavingsaccountComponent } from './savingsaccount.component';
import { SavingsaccountDetailComponent } from './savingsaccount-detail.component';
import { SavingsaccountUpdateComponent } from './savingsaccount-update.component';
import { SavingsaccountDeletePopupComponent } from './savingsaccount-delete-dialog.component';
import { ISavingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';

@Injectable({ providedIn: 'root' })
export class SavingsaccountResolve implements Resolve<ISavingsaccount> {
    constructor(private service: SavingsaccountService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((savingsaccount: HttpResponse<Savingsaccount>) => savingsaccount.body);
        }
        return Observable.of(new Savingsaccount());
    }
}

export const savingsaccountRoute: Routes = [
    {
        path: 'savingsaccount',
        component: SavingsaccountComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Savingsaccounts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'savingsaccount/:id/view',
        component: SavingsaccountDetailComponent,
        resolve: {
            savingsaccount: SavingsaccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Savingsaccounts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'savingsaccount/new',
        component: SavingsaccountUpdateComponent,
        resolve: {
            savingsaccount: SavingsaccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Savingsaccounts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'savingsaccount/:id/edit',
        component: SavingsaccountUpdateComponent,
        resolve: {
            savingsaccount: SavingsaccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Savingsaccounts'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const savingsaccountPopupRoute: Routes = [
    {
        path: 'savingsaccount/:id/delete',
        component: SavingsaccountDeletePopupComponent,
        resolve: {
            savingsaccount: SavingsaccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Savingsaccounts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

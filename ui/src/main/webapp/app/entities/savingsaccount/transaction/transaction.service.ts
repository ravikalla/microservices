import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITransaction } from 'app/shared/model/savingsaccount/transaction.model';

type EntityResponseType = HttpResponse<ITransaction>;
type EntityArrayResponseType = HttpResponse<ITransaction[]>;

@Injectable({ providedIn: 'root' })
export class TransactionService {
    private resourceUrl = SERVER_API_URL + 'savingsaccount/api/transactions';

    constructor(private http: HttpClient) {}

    create(transaction: ITransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(transaction);
        return this.http
            .post<ITransaction>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    update(transaction: ITransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(transaction);
        return this.http
            .put<ITransaction>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(transaction: ITransaction): ITransaction {
        const copy: ITransaction = Object.assign({}, transaction, {
            transactiondate:
                transaction.transactiondate != null && transaction.transactiondate.isValid()
                    ? transaction.transactiondate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.transactiondate = res.body.transactiondate != null ? moment(res.body.transactiondate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((transaction: ITransaction) => {
            transaction.transactiondate = transaction.transactiondate != null ? moment(transaction.transactiondate) : null;
        });
        return res;
    }
}

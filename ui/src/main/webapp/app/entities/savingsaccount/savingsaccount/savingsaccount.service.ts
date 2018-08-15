import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISavingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';

type EntityResponseType = HttpResponse<ISavingsaccount>;
type EntityArrayResponseType = HttpResponse<ISavingsaccount[]>;

@Injectable({ providedIn: 'root' })
export class SavingsaccountService {
    private resourceUrl = SERVER_API_URL + 'savingsaccount/api/savingsaccounts';

    constructor(private http: HttpClient) {}

    create(savingsaccount: ISavingsaccount): Observable<EntityResponseType> {
        return this.http.post<ISavingsaccount>(this.resourceUrl, savingsaccount, { observe: 'response' });
    }

    update(savingsaccount: ISavingsaccount): Observable<EntityResponseType> {
        return this.http.put<ISavingsaccount>(this.resourceUrl, savingsaccount, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISavingsaccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISavingsaccount[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

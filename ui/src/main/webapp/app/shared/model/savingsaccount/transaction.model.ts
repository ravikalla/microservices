import { Moment } from 'moment';

export interface ITransaction {
    id?: number;
    amount?: number;
    transactiondate?: Moment;
    savingsaccountAccountnumber?: string;
    savingsaccountId?: number;
}

export class Transaction implements ITransaction {
    constructor(
        public id?: number,
        public amount?: number,
        public transactiondate?: Moment,
        public savingsaccountAccountnumber?: string,
        public savingsaccountId?: number
    ) {}
}

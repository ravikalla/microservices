export interface ISavingsaccount {
    id?: number;
    accountnumber?: number;
    accountname?: string;
    amount?: number;
    generalinfo?: string;
}

export class Savingsaccount implements ISavingsaccount {
    constructor(
        public id?: number,
        public accountnumber?: number,
        public accountname?: string,
        public amount?: number,
        public generalinfo?: string
    ) {}
}

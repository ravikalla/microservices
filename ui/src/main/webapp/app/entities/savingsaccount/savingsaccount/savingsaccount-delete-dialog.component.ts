import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISavingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';
import { SavingsaccountService } from './savingsaccount.service';

@Component({
    selector: 'jhi-savingsaccount-delete-dialog',
    templateUrl: './savingsaccount-delete-dialog.component.html'
})
export class SavingsaccountDeleteDialogComponent {
    savingsaccount: ISavingsaccount;

    constructor(
        private savingsaccountService: SavingsaccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.savingsaccountService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'savingsaccountListModification',
                content: 'Deleted an savingsaccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-savingsaccount-delete-popup',
    template: ''
})
export class SavingsaccountDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ savingsaccount }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SavingsaccountDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.savingsaccount = savingsaccount;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}

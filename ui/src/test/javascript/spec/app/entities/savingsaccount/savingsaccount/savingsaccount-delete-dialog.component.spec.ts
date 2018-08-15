/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UiTestModule } from '../../../../test.module';
import { SavingsaccountDeleteDialogComponent } from 'app/entities/savingsaccount/savingsaccount/savingsaccount-delete-dialog.component';
import { SavingsaccountService } from 'app/entities/savingsaccount/savingsaccount/savingsaccount.service';

describe('Component Tests', () => {
    describe('Savingsaccount Management Delete Component', () => {
        let comp: SavingsaccountDeleteDialogComponent;
        let fixture: ComponentFixture<SavingsaccountDeleteDialogComponent>;
        let service: SavingsaccountService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UiTestModule],
                declarations: [SavingsaccountDeleteDialogComponent]
            })
                .overrideTemplate(SavingsaccountDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SavingsaccountDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SavingsaccountService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});

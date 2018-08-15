/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UiTestModule } from '../../../../test.module';
import { SavingsaccountUpdateComponent } from 'app/entities/savingsaccount/savingsaccount/savingsaccount-update.component';
import { SavingsaccountService } from 'app/entities/savingsaccount/savingsaccount/savingsaccount.service';
import { Savingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';

describe('Component Tests', () => {
    describe('Savingsaccount Management Update Component', () => {
        let comp: SavingsaccountUpdateComponent;
        let fixture: ComponentFixture<SavingsaccountUpdateComponent>;
        let service: SavingsaccountService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UiTestModule],
                declarations: [SavingsaccountUpdateComponent]
            })
                .overrideTemplate(SavingsaccountUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SavingsaccountUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SavingsaccountService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Savingsaccount(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.savingsaccount = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Savingsaccount();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.savingsaccount = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});

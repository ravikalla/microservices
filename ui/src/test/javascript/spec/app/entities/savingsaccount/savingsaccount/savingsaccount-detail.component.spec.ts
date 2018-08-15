/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UiTestModule } from '../../../../test.module';
import { SavingsaccountDetailComponent } from 'app/entities/savingsaccount/savingsaccount/savingsaccount-detail.component';
import { Savingsaccount } from 'app/shared/model/savingsaccount/savingsaccount.model';

describe('Component Tests', () => {
    describe('Savingsaccount Management Detail Component', () => {
        let comp: SavingsaccountDetailComponent;
        let fixture: ComponentFixture<SavingsaccountDetailComponent>;
        const route = ({ data: of({ savingsaccount: new Savingsaccount(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UiTestModule],
                declarations: [SavingsaccountDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SavingsaccountDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SavingsaccountDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.savingsaccount).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

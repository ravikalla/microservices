import { NgModule } from '@angular/core';

import { UiSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [UiSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [UiSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class UiSharedCommonModule {}

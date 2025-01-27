import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  GoogleMap,
  GoogleMapsModule,
  MapRectangle,
} from '@angular/google-maps';
import { TranslateModule } from '@ngx-translate/core';
import { NgxGaugeModule } from 'ngx-gauge';
import { NgZorroModule } from './ng-zorro.module';
@NgModule({
  declarations: [],
  imports: [CommonModule, GoogleMap, MapRectangle],
  exports: [
    NgZorroModule,
    NgxGaugeModule,
    CommonModule,
    GoogleMapsModule,
    GoogleMap,
    MapRectangle,
    TranslateModule,
    FormsModule,
    ReactiveFormsModule,
  ],
})
export class SharedModule {}

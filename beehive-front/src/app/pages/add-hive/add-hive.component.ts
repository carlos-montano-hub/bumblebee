import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SharedModule } from '../../modules/shared.module';
import { Apiary } from '../../services/apiaries/apiary.model';
import { ApiaryService } from '../../services/apiaries/apiary.service';
import { BeehiveService } from '../../services/beehives/beehive.service';
@Component({
  selector: 'app-add-hive',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './add-hive.component.html',
  styleUrls: ['./add-hive.component.css'],
})
export class AddHiveComponent implements OnInit {
  currentStep = 0;
  serialForm: FormGroup;
  selectedApiary?: Apiary;
  apiaries: Apiary[] = [];
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private readonly apiaryService: ApiaryService,
    private readonly beehiveService: BeehiveService,
    public router: Router,
    private translateService: TranslateService
  ) {
    this.serialForm = this.fb.group({
      serial: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  async ngOnInit() {
    this.apiaries = await this.apiaryService.getApiariesByUser();
  }

  onSerialSubmit() {
    if (this.serialForm.valid) {
      this.currentStep = 1;
      this.beehiveService
        .checkHiveSerial({
          serial: this.serialForm.get('serial')?.value,
        })
        .then(() => {
          this.currentStep = 2;
        })
        .catch(() => {
          this.currentStep = 0;
          alert(this.translateService.instant('Beehive.SERIAL_ERROR'));
        });
    }
  }

  onApiarySubmit() {
    if (this.selectedApiary) {
      this.registerBeehive();
    }
  }

  async registerBeehive() {
    if (this.serialForm.invalid || !this.selectedApiary) return;

    this.isLoading = true;
    const serial = this.serialForm.get('serial')?.value;
    const apiaryId = this.selectedApiary.id;
    const name = `Beehive-${serial}`; // Example name generation logic

    try {
      await this.beehiveService.registerNewHive({ serial, apiaryId, name });
      this.currentStep = 3; // Move to success step
    } catch (error) {
      console.error('Error registering beehive:', error);
      // Handle error (show a message, stay on current step, etc.)
    } finally {
      this.isLoading = false;
    }
  }

  onDone() {
    this.router.navigate(['status']);
  }
}

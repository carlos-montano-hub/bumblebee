import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SharedModule } from '../../modules/shared.module';
import { ApiaryService } from '../../services/apiaries/apiary.service';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-add-apiary',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './add-apiary.component.html',
  styleUrl: './add-apiary.component.css',
})
export class AddApiaryComponent {
  apiaryForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private readonly apiaryService: ApiaryService,
    private readonly authService: AuthService,
    public router: Router
  ) {
    this.apiaryForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  async onApiarySubmit() {
    try {
      const name = this.apiaryForm.get('name')?.value;

      if (!name) {
        alert('Name required to create an apiary.');
        return;
      }

      await this.apiaryService.createApiary(name);
      this.router.navigate(['status']);

      alert('Apiary created successfully!');
    } catch (err) {
      console.error('Failed to create apiary', err);
      alert('An error occurred while creating the apiary.');
    }
  }
}

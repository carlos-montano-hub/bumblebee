import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SharedModule } from '../../modules/shared.module';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [SharedModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private translateService: TranslateService
  ) {
    this.registerForm = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(50),
        ],
      ],
      phoneNumber: [
        '',
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(15),
        ],
      ],
      emailAddress: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  async ngOnInit() {
    if (await this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }

  async onSubmit() {
    if (this.registerForm.valid) {
      try {
        const form = this.registerForm.value;
        await this.authService.register(form);
        alert(this.translateService.instant('Login.REGISTRATION_SUCCESS'));
        this.router.navigate(['/']);
      } catch (err) {
        this.errorMessage = 'Login.ERROR_INVALID_CREDENTIALS';
        console.error(err);
      }
    } else {
      this.errorMessage = 'Login.ERROR_FILL_ALL_FIELDS';
    }
  }

  onLogin() {
    this.router.navigate(['login']);
  }
}

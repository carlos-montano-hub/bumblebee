import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedModule } from '../../modules/shared.module';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
})
export class LoginPageComponent implements OnInit {
  email?: string;
  password?: string;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  async ngOnInit() {
    if (await this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }

  async onSubmit() {
    try {
      await this.authService.login(this.email ?? '', this.password ?? '');
      this.router.navigate(['/']);
    } catch (err) {
      this.errorMessage = 'Invalid email or password';
      console.error(err);
    }
  }

  onRegister() {
    this.router.navigate(['register']);
  }
}

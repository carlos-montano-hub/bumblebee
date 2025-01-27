import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedModule } from '../../modules/shared.module';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css',
})
export class HomePageComponent {
  constructor(private router: Router, private authService: AuthService) {}

  navigate(route: string) {
    this.router.navigate([route]);
  }

  logOut() {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}

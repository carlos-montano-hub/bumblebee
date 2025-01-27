import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  async canActivate() {
    const isAuthenticated = await this.authService.isAuthenticated();
    if (isAuthenticated) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }
}

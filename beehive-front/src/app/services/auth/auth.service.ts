import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { NativeHttpService } from '../native-http-client.service';
import { User } from '../user/user.model';
import { TokenService } from './token.service';

export type RegisterForm = {
  name: string;
  phoneNumber: string;
  emailAddress: string;
  password: string;
};
export type LoginResponse = {
  token: string;
  refreshToken: string;
};
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl: string = 'http://localhost:8080/api/appUsers';
  private _profile?: User;

  constructor(
    private http: NativeHttpService,
    private tokenService: TokenService,
    private router: Router // Inject Router
  ) {}

  async getProfile() {
    if ((await this.tokenService.getToken()) && !this._profile) {
      await this.fetchProfile();
    }
    return this._profile;
  }

  async fetchProfile() {
    const token = await this.tokenService.getToken();
    if (token) {
      const headers = { Authorization: `Bearer ${token}` };
      try {
        this._profile = await this.http.get<User>(
          'http://localhost:8080/auth/profile',
          { headers }
        );
      } catch (err) {
        console.error('Failed to load profile', err);
        this._profile = undefined;
        await this.logout(); // Redirect if unauthorized
      }
    }
  }

  async login(email: string, password: string) {
    const url = `http://localhost:8080/auth/login`;
    const body = { email, password };
    try {
      const response = await this.http.post<LoginResponse>(
        url,
        body,
        {},
        false
      );
      await this.tokenService.setToken(response.token);
      await this.tokenService.setRefreshToken(response.refreshToken);
      await this.fetchProfile(); // Load profile after login
      this.router.navigate(['']); // Redirect to home page
    } catch (err) {
      console.log('Login failed', err);
      throw err; // Rethrow error for UI handling
    }
  }

  async register(form: RegisterForm) {
    const url = `http://localhost:8080/auth/register`;

    try {
      await this.http.post<{
        token: string;
        refreshToken: string;
      }>(url, form, {}, false);
      this.router.navigate(['/login']); // Redirect to login page
    } catch (err) {
      console.error('Register failed', err);
      throw err; // Rethrow error for UI handling
    }
  }

  async isAuthenticated() {
    const tokenExists = !!(await this.tokenService.getToken());
    return tokenExists;
  }

  async logout() {
    await this.tokenService.clearToken();
    this._profile = undefined;
    this.router.navigate(['/login']); // Redirect to login page
  }
}

import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {
  Capacitor,
  CapacitorHttp,
  HttpOptions,
  HttpResponse,
} from '@capacitor/core';
import { firstValueFrom } from 'rxjs';
import { TokenService } from './auth/token.service';
@Injectable({
  providedIn: 'root',
})
export class NativeHttpService {
  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router // Added router
  ) {}

  private isNative(): boolean {
    return Capacitor.isNativePlatform();
  }

  private async getAuthHeaders() {
    const token = await this.tokenService.getToken();
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  private async refreshAuthToken(): Promise<void> {
    const refreshUrl = 'http://localhost:8080/auth/refresh';

    const refreshToken = await this.tokenService.getRefreshToken();

    if (!refreshToken) {
      this.handleUnauthenticated();
      throw new Error('No refresh token available');
    }

    try {
      let response;
      if (this.isNative()) {
        const options: HttpOptions = {
          url: refreshUrl,
          headers: { 'Content-Type': 'application/json' },
          data: { refreshToken },
        };
        response = await CapacitorHttp.post(options);
        response = this.handleCapacitorResponse(response);
      } else {
        response = await firstValueFrom(
          this.http.post<any>(refreshUrl, {
            refreshToken,
          })
        );
      }

      await this.tokenService.setToken(response.token);
      await this.tokenService.setRefreshToken(response.refreshToken);
    } catch (error) {
      this.handleUnauthenticated();
      throw error;
    }
  }

  private async ensureToken(): Promise<void> {
    if (await this.tokenService.isTokenExpired()) {
      await this.refreshAuthToken();
    }
  }

  private handleUnauthenticated(): void {
    this.tokenService.clearToken(); // Clear any invalid tokens
    this.router.navigate(['/login']); // Redirect to the login page
  }

  async get<T>(
    url: string,
    options: { responseType?: string; headers?: any } = {}
  ): Promise<T> {
    await this.ensureToken();
    const authHeaders = await this.getAuthHeaders();
    const completeHeaders = {
      ...options.headers,
      ...authHeaders,
    };

    if (!completeHeaders['Content-Type'] && options.responseType !== 'blob') {
      completeHeaders['Content-Type'] = 'application/json';
    }

    if (this.isNative()) {
      const nativeOptions: HttpOptions = {
        url,
        headers: completeHeaders,
        responseType: options.responseType as any,
      };

      const response = await CapacitorHttp.get(nativeOptions);

      if (options.responseType === 'blob') {
        // Handle binary data for native platform
        const base64Data = response.data;
        const binaryString = window.atob(base64Data);
        const bytes = new Uint8Array(binaryString.length);
        for (let i = 0; i < binaryString.length; i++) {
          bytes[i] = binaryString.charCodeAt(i);
        }
        return new Blob([bytes], { type: 'audio/wav' }) as any;
      }

      return this.handleCapacitorResponse(response);
    } else {
      if (options.responseType === 'blob') {
        const response = await firstValueFrom(
          this.http.get(url, {
            headers: new HttpHeaders(completeHeaders),
            responseType: 'blob',
          })
        );
        return response as any;
      }
      return firstValueFrom(
        this.http.get<T>(url, { headers: completeHeaders })
      );
    }
  }

  async post<T>(
    url: string,
    data: any,
    headers: any = {},
    auth = true
  ): Promise<T> {
    if (auth) {
      await this.ensureToken();
    }
    const authHeaders = await this.getAuthHeaders();
    const completeHeaders = {
      ...headers,
      ...authHeaders,
      'Content-Type': 'application/json',
    };

    try {
      if (this.isNative()) {
        const options: HttpOptions = { url, headers: completeHeaders, data };
        const response = await CapacitorHttp.post(options);
        console.log(JSON.stringify(response));
        return this.handleCapacitorResponse(response);
      } else {
        return await firstValueFrom(
          this.http.post<T>(url, data, {
            headers: new HttpHeaders(completeHeaders),
          })
        );
      }
    } catch (err) {
      throw err;
    }
  }

  async put<T>(url: string, data: any, headers: any = {}): Promise<T> {
    await this.ensureToken();
    const authHeaders = {
      ...headers,
      ...this.getAuthHeaders(),
      'Content-Type': 'application/json',
    };

    if (this.isNative()) {
      const options: HttpOptions = { url, headers: authHeaders, data };
      const response = await CapacitorHttp.put(options);
      return this.handleCapacitorResponse(response);
    } else {
      return firstValueFrom(
        this.http.put<T>(url, data, {
          headers: new HttpHeaders(authHeaders),
        })
      );
    }
  }

  async delete<T>(url: string, headers: any = {}): Promise<T> {
    await this.ensureToken();
    const authHeaders = {
      ...headers,
      ...this.getAuthHeaders(),
      'Content-Type': 'application/json',
    };

    if (this.isNative()) {
      const options: HttpOptions = { url, headers: authHeaders };
      const response = await CapacitorHttp.delete(options);
      return this.handleCapacitorResponse(response);
    } else {
      return firstValueFrom(this.http.delete<T>(url, { headers: authHeaders }));
    }
  }

  private async handleCapacitorResponse(response: HttpResponse) {
    if (response.status >= 200 && response.status < 300) {
      return response.data;
    } else {
      throw new HttpErrorResponse({
        error: response.data,
        status: response.status,
        url: response.url,
      });
    }
  }
}

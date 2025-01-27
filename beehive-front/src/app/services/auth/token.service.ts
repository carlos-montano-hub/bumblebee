import { Injectable } from '@angular/core';
import { StorageService } from '../storage.service';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private tokenKey = 'authToken';
  private refreshTokenKey = 'refreshToken';
  private expirationKey = 'tokenExpiration';
  private _expirationTime = 1000 * 60 * 60; // 1 hour in milliseconds

  constructor(private storage: StorageService) {}

  get expirationTime(): number {
    return this._expirationTime;
  }

  async setToken(token: string): Promise<void> {
    const expiration = new Date().getTime() + this._expirationTime;
    await this.storage.setItem(this.tokenKey, token);
    await this.storage.setItem(this.expirationKey, expiration.toString());
  }

  async getToken(): Promise<string | null> {
    const token = await this.storage.getItem(this.tokenKey);
    return token;
  }

  async isTokenExpired(): Promise<boolean> {
    const expiration = await this.storage.getItem(this.expirationKey);
    return expiration ? new Date().getTime() > parseInt(expiration, 10) : true;
  }

  async setRefreshToken(refreshToken: string): Promise<void> {
    await this.storage.setItem(this.refreshTokenKey, refreshToken);
  }

  async getRefreshToken(): Promise<string | null> {
    return this.storage.getItem(this.refreshTokenKey);
  }

  async clearToken(): Promise<void> {
    try {
      await this.storage.removeItem(this.tokenKey);
      await this.storage.removeItem(this.refreshTokenKey);
      await this.storage.removeItem(this.expirationKey);
    } catch (_) {}
  }
}

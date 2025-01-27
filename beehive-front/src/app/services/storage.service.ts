import { Injectable } from '@angular/core';
import { Capacitor } from '@capacitor/core';
import { Preferences } from '@capacitor/preferences';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  constructor() {}

  private isNative(): boolean {
    return Capacitor.isNativePlatform();
  }

  async setItem(key: string, value: string): Promise<void> {
    if (this.isNative()) {
      await Preferences.set({ key, value });
    } else {
      localStorage.setItem(key, value);
    }
  }

  async getItem(key: string) {
    if (this.isNative()) {
      const { value } = await Preferences.get({ key });
      return value;
    } else {
      const item = localStorage.getItem(key);
      return item;
    }
  }

  async removeItem(key: string): Promise<void> {
    if (this.isNative()) {
      await Preferences.remove({ key });
    } else {
      localStorage.removeItem(key);
    }
  }

  async clear(): Promise<void> {
    if (this.isNative()) {
      await Preferences.clear();
    } else {
      localStorage.clear();
    }
  }

  async keys(): Promise<string[]> {
    if (this.isNative()) {
      const { keys } = await Preferences.keys();
      return keys;
    } else {
      return Object.keys(localStorage);
    }
  }
}

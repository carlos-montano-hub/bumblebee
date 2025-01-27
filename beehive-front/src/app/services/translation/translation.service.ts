import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { StorageService } from '../storage.service';
import { Language, languages } from './languages-list';

@Injectable({
  providedIn: 'root',
})
export class TranslationService {
  private _currentLanguage: Language = languages[0];
  private readonly languageStorageKey = 'selectedLanguage';

  constructor(
    private readonly translateService: TranslateService,
    private readonly storageService: StorageService,
    @Inject(PLATFORM_ID) private readonly platformId: Object
  ) {
    this.setUp();
  }

  private async setUp() {
    if (isPlatformBrowser(this.platformId)) {
      const savedLangKey = await this.storageService.getItem(
        this.languageStorageKey
      );
      if (savedLangKey) {
        this.setLanguage(savedLangKey);
      }
      this.translateService.setDefaultLang(this._currentLanguage.key);
    }
  }

  get currentLanguage() {
    return this._currentLanguage;
  }

  private setLanguage(langKey: string) {
    this._currentLanguage =
      languages.find((lang) => lang.key === langKey) ?? languages[0];
    this.translateService.use(this._currentLanguage.key);
  }

  async changeLang(langKey: string) {
    this.setLanguage(langKey);
    if (isPlatformBrowser(this.platformId)) {
      await this.storageService.setItem(
        this.languageStorageKey,
        this._currentLanguage.key
      );
    }
  }
}

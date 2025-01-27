import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedModule } from '../../modules/shared.module';
import { Language, languages } from '../../services/translation/languages-list';
import { TranslationService } from '../../services/translation/translation.service';
@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css',
})
export class NavBarComponent implements OnInit {
  isMenuOpen = false;
  visible = false;
  currentLanguage?: Language;
  languages: Language[] = [];

  constructor(
    private readonly translationService: TranslationService,
    public router: Router
  ) {}

  ngOnInit() {
    this.languages = languages;
    this.currentLanguage = this.translationService.currentLanguage;
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  changeLanguage(lang: Language) {
    this.currentLanguage = this.translationService.currentLanguage;
    this.translationService.changeLang(lang.key);
  }
}

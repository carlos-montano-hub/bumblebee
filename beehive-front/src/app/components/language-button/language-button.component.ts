import { Component } from '@angular/core';

@Component({
  selector: 'app-language-button',
  standalone: true,
  imports: [],
  templateUrl: './language-button.component.html',
  styleUrl: './language-button.component.css',
})
export class LanguageButtonComponent {
  currentLanguage = 'EN';
  languages = ['EN', 'ES', 'FR', 'DE'];

  changeLanguage() {
    const currentIndex = this.languages.indexOf(this.currentLanguage);
    const nextIndex = (currentIndex + 1) % this.languages.length;
    this.currentLanguage = this.languages[nextIndex];
  }
}

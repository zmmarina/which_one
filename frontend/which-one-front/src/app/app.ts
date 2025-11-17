import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgFor } from '@angular/common'; 
import { TranslateService, TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TranslateModule, NgFor],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {

  protected readonly title = signal('which-one-front');

  protected readonly langs = [
  { code: 'en', label: 'English' },
  { code: 'pt', label: 'Português' },
  { code: 'es', label: 'Español' },
  { code: 'fr', label: 'Français' },
  { code: 'hi', label: 'हिन्दी' },
  { code: 'zh', label: '中文' }
];

  constructor(private translate: TranslateService) {

    this.translate.addLangs(['en', 'pt', 'es', 'fr', 'hi', 'zh']);
    this.translate.setDefaultLang('en');

    const browserLang = navigator.language.split('-')[0];

    if (['en', 'pt', 'es', 'fr', 'hi', 'zh'].includes(browserLang)) {
      this.translate.use(browserLang);
    } else {
      this.translate.use('en');
    }
  }
    changeLang(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.translate.use(selectElement.value);
  }
}
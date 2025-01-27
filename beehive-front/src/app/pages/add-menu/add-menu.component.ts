import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedModule } from '../../modules/shared.module';

@Component({
  selector: 'app-add-menu',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './add-menu.component.html',
  styleUrl: './add-menu.component.css',
})
export class AddMenuComponent {
  constructor(private router: Router) {}
  navigate(route: string) {
    this.router.navigate([route]);
  }
}

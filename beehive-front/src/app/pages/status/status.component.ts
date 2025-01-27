import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedModule } from '../../modules/shared.module';
import { Apiary } from '../../services/apiaries/apiary.model';
import { ApiaryService } from '../../services/apiaries/apiary.service';
import { beehivesTestData } from '../../services/beehives/beehive.model';
import { ApiaryCardComponent } from './apiary-card/apiary-card.component';

@Component({
  selector: 'app-status',
  standalone: true,
  imports: [SharedModule, ApiaryCardComponent],
  templateUrl: './status.component.html',
  styleUrl: './status.component.css',
})
export class StatusComponent implements OnInit {
  beehives = beehivesTestData;
  isVisibleTop = false;
  isVisibleMiddle = false;
  apiaries: Apiary[] = [];

  constructor(
    private readonly apiaryService: ApiaryService,
    private router: Router
  ) {}

  navigate(route: string) {
    this.router.navigate([route]);
  }
  async ngOnInit() {
    this.apiaries = await this.apiaryService.getApiariesByUser();
  }
}

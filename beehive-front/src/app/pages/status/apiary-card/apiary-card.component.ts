import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { SharedModule } from '../../../modules/shared.module';
import { Apiary } from '../../../services/apiaries/apiary.model';
import { ApiaryService } from '../../../services/apiaries/apiary.service';
import { beehivesTestData } from '../../../services/beehives/beehive.model';
import { BeehiveService } from '../../../services/beehives/beehive.service';
import { BeehiveCardComponent } from './beehive-card/beehive-card.component';

@Component({
  selector: 'app-apiary-card',
  standalone: true,
  imports: [SharedModule, BeehiveCardComponent],
  templateUrl: './apiary-card.component.html',
  styleUrl: './apiary-card.component.css',
})
export class ApiaryCardComponent {
  @Input() apiary!: Apiary;
  beehives = beehivesTestData;

  constructor(
    private readonly beehiveService: BeehiveService,
    private readonly apiaryService: ApiaryService,
    private router: Router
  ) {}

  async ngOnInit() {
    this.beehives = await this.beehiveService.getBeehivesByApiaryId(
      this.apiary.id
    );
  }

  navigate(route: string) {
    this.router.navigate([route]);
  }
}

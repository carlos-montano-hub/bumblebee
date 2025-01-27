import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { MapComponentComponent } from '../../../../components/map-component/map-component.component';
import { SharedModule } from '../../../../modules/shared.module';
import { Beehive } from '../../../../services/beehives/beehive.model';

@Component({
  selector: 'app-beehive-card',
  standalone: true,
  imports: [SharedModule, MapComponentComponent],
  templateUrl: './beehive-card.component.html',
  styleUrl: './beehive-card.component.css',
})
export class BeehiveCardComponent {
  @Input() beehive?: Beehive;
  constructor(private router: Router) {}
  navigateToHiveDetails(hiveId: number): void {
    this.router.navigate(['/hive-details', hiveId]);
  }
  hasEvent() {
    const isTempBad =
      this.beehive?.lastMeasure?.temperature &&
      (this.beehive.lastMeasure.temperature < 33 ||
        this.beehive.lastMeasure.temperature > 36);
    const isHumidityBad =
      this.beehive?.lastMeasure?.humidity &&
      (this.beehive.lastMeasure.humidity < 50 ||
        this.beehive.lastMeasure.humidity > 60);
    return isTempBad || isHumidityBad;
  }

  thresholds = {
    '0': {
      color: 'cyan',
      bgOpacity: 0.2,
    },
    '33': {
      color: 'green',
      bgOpacity: 0.2,
    },
    '36': {
      color: '#cc0000',
      bgOpacity: 0.2,
    },
  };
}

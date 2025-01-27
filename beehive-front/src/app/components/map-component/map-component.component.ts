import { Component, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../modules/shared.module';
@Component({
  selector: 'app-map-component',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './map-component.component.html',
  styleUrl: './map-component.component.css',
})
export class MapComponentComponent implements OnInit {
  @Input() latitude!: number;
  @Input() longitude!: number;

  mapOptions?: any;
  markerOptions = { draggable: false };
  bounds = {
    east: 10,
    north: 10,
    south: -10,
    west: -10,
  };

  async ngOnInit() {
    try {
      if (typeof google === 'object' && typeof google.maps === 'object') {
        this.mapOptions = {
          center: { lat: this.latitude, lng: this.longitude },
          zoom: 10,
          disableDefaultUI: true,
          gestureHandling: 'none',
          renderingType: google.maps.RenderingType.VECTOR,
        };
      }
    } catch (error) {
      console.error('Error loading Google Maps API:', error);
    }
  }
}

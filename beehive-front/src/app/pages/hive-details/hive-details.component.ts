import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Color, NgxChartsModule, ScaleType } from '@swimlane/ngx-charts';
import WaveSurfer from 'wavesurfer.js';
// import TimelinePlugin from 'wavesurfer.js/dist/plugins/timeline.js';
import SpectrogramPlugin from 'wavesurfer.js/dist/plugins/spectrogram.js';
import { SharedModule } from '../../modules/shared.module';
import { Beehive } from '../../services/beehives/beehive.model';
import { BeehiveService } from '../../services/beehives/beehive.service';
import { AudioService } from '../../services/measure/audio.service';
import { Measure } from '../../services/measure/measure.model';
import { MeasureService } from '../../services/measure/measure.service';
@Component({
  selector: 'app-hive-details',
  standalone: true,
  imports: [SharedModule, NgxChartsModule],
  templateUrl: './hive-details.component.html',
  styleUrl: './hive-details.component.css',
})
export class HiveDetailsComponent implements OnInit {
  wave: WaveSurfer | null = null;
  url = 'assets/audio/beehive_sound.wav'; // Update this with your actual audio file path
  volume: number = 10;
  isBoosted: boolean = true;

  beehiveId: number;
  measures: Measure[] = [];
  latestMeasure?: Measure;
  beehive?: Beehive;

  temperatureData: any[] = [];
  humidityData: any[] = [];
  weightData: any[] = [];

  view: [number, number] = [700, 300];
  xAxisLabel: string = 'Time';

  colorScheme: Color = {
    name: 'myScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#FF6B6B', '#4ECDC4', '#45B7D1'],
  };

  constructor(
    private measureService: MeasureService,
    private route: ActivatedRoute,
    private readonly beehiveService: BeehiveService,
    private cdr: ChangeDetectorRef,
    private readonly audioService: AudioService
  ) {
    this.beehiveId = Number(this.route.snapshot.paramMap.get('id')) || 0;
  }

  async ngOnInit(): Promise<void> {
    await this.fetchMeasures();
    this.onResize(window.innerWidth);
  }

  async fetchMeasures(): Promise<void> {
    try {
      this.measures = await this.measureService.getMeasuresByBeehiveId(
        this.beehiveId
      );
      this.beehive = await this.beehiveService.getBeehive(this.beehiveId);
      this.latestMeasure = this.beehive.lastMeasure;
      const audioBlob = await this.audioService.getAudioBlob(
        this.latestMeasure?.audioRecordingUrl ?? ''
      );
      this.url = URL.createObjectURL(audioBlob);
      // this.url =
      //   'http://localhost:8080/api/measure/audio/' +
      //   this.latestMeasure?.audioRecordingUrl;
      this.updateChartData();
      this.initWaveform();
    } catch (error) {
      console.error('Error fetching measures:', error);
      // Handle error (e.g., show error message to user)
    }
  }

  updateChartData(): void {
    this.temperatureData = [
      {
        name: 'Temperature',
        series: this.measures.map((m) => ({
          name: new Date(m.time),
          value: m.temperature,
        })),
      },
    ];

    this.humidityData = [
      {
        name: 'Humidity',
        series: this.measures.map((m) => ({
          name: new Date(m.time),
          value: m.humidity,
        })),
      },
    ];

    this.weightData = [
      {
        name: 'Weight',
        series: this.measures.map((m) => ({
          name: new Date(m.time),
          value: m.weight,
        })),
      },
    ];
  }

  onResize(width: number): void {
    this.view = [width - 20, 300];
  }

  initWaveform(): void {
    Promise.resolve(null).then(() => {
      this.wave = WaveSurfer.create({
        container: '#waveform',
        waveColor: '#1890ff',
        progressColor: '#001529',
        responsive: true,
        barHeight: 50,
        plugins: [
          SpectrogramPlugin.create({
            container: '#wave-spectrogram',
            labels: true,
          }),
        ],
      });

      this.wave.on('ready', () => {
        console.log('WaveSurfer is ready');
        this.setVolume(this.volume);
      });

      this.wave.load(this.url);
    });
  }

  onPlayPausePressed(): void {
    if (this.wave) {
      this.wave.playPause();
    }
  }

  onStopPressed(): void {
    if (this.wave) {
      this.wave.stop();
    }
  }

  setVolume(value: number): void {
    this.volume = value;
    if (this.wave) {
      this.wave.setVolume(this.isBoosted ? this.volume * 2 : this.volume);
    }
  }

  toggleVolumeBoost(): void {
    this.isBoosted = !this.isBoosted;
    this.setVolume(this.volume);
  }
}

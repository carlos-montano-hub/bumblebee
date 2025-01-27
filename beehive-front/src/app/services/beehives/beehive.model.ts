import { Apiary } from '../apiaries/apiary.model';
import { Measure } from '../measure/measure.model';

export interface Beehive {
  id: number;
  name: string;
  serial: string;
  latitude: number;
  longitude: number;
  apiary?: Apiary;
  lastMeasure?: Measure;
}

export const beehivesTestData: Beehive[] = [
  {
    id: 1,
    name: 'Hive A',
    serial: 'A001',
    latitude: 29.0636,
    longitude: -111.1,
    lastMeasure: {
      id: 1,
      time: new Date('2024-11-01T10:00:00Z'),
      temperature: 35.2,
      humidity: 60.5,
      weight: 75.3,
      audioRecordingUrl: 'http://example.com/audio/hiveA/lastMeasure.mp3',
    },
  },
  {
    id: 2,
    name: 'Hive B',
    serial: 'B002',
    latitude: 19.4512,
    longitude: -99.1453,
    lastMeasure: {
      id: 2,
      time: new Date('2024-11-01T11:00:00Z'),
      temperature: 33.8,
      humidity: 58.2,
      weight: 80.1,
      audioRecordingUrl: 'http://example.com/audio/hiveB/lastMeasure.mp3',
    },
  },
  {
    id: 3,
    name: 'Hive C',
    serial: 'C003',
    latitude: 19.4171,
    longitude: -99.1277,
    lastMeasure: {
      id: 3,
      time: new Date('2024-11-01T12:00:00Z'),
      temperature: 36.0,
      humidity: 62.0,
      weight: 70.8,
      audioRecordingUrl: 'http://example.com/audio/hiveC/lastMeasure.mp3',
    },
  },
  {
    id: 4,
    name: 'Hive D',
    serial: 'D004',
    latitude: 19.4394,
    longitude: -99.1623,
    lastMeasure: {
      id: 4,
      time: new Date('2024-11-01T13:00:00Z'),
      temperature: 34.5,
      humidity: 59.8,
      weight: 72.4,
      audioRecordingUrl: 'http://example.com/audio/hiveD/lastMeasure.mp3',
    },
  },
  {
    id: 5,
    name: 'Hive E',
    serial: 'E005',
    latitude: 19.4289,
    longitude: -99.1346,
    lastMeasure: {
      id: 5,
      time: new Date('2024-11-01T14:00:00Z'),
      temperature: 32.9,
      humidity: 57.3,
      weight: 78.6,
      audioRecordingUrl: 'http://example.com/audio/hiveE/lastMeasure.mp3',
    },
  },
];

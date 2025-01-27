import { Injectable } from '@angular/core';
import { NativeHttpService } from '../native-http-client.service';

@Injectable({
  providedIn: 'root',
})
export class AudioService {
  constructor(private nativeHttp: NativeHttpService) {}

  async getAudioBlob(url: string): Promise<Blob> {
    try {
      const audioBlob = await this.nativeHttp.get<Blob>(
        'http://localhost:8080/api/measure/audio/' + url,
        {
          responseType: 'blob',
        }
      );

      return audioBlob;
    } catch (error) {
      console.error('Error fetching audio:', error);
      throw error;
    }
  }
}

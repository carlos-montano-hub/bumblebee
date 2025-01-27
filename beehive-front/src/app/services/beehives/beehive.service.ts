import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NativeHttpService } from '../native-http-client.service';
import { Beehive } from './beehive.model';

@Injectable({
  providedIn: 'root',
})
export class BeehiveService {
  private baseUrl: string = 'http://localhost:8080/api/beehives';

  constructor(private http: NativeHttpService) {}

  // Get all beehives
  async getBeehives(): Promise<Beehive[]> {
    return this.http.get<Beehive[]>(this.baseUrl);
  }

  // Get a single beehive by ID
  async getBeehive(id: number): Promise<Beehive> {
    return this.http.get<Beehive>(`${this.baseUrl}/${id}`);
  }

  // Create a new beehive
  async createBeehive(beehive: Beehive): Promise<Beehive> {
    return this.http.post<Beehive>(this.baseUrl, beehive, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // Update an existing beehive
  async updateBeehive(beehive: Beehive): Promise<Beehive> {
    return this.http.put<Beehive>(`${this.baseUrl}/${beehive.id}`, beehive, {
      'Content-Type': 'application/json',
    });
  }

  // Delete a beehive by ID
  async deleteBeehive(id: number): Promise<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Get beehives by apiary ID
  async getBeehivesByApiaryId(apiaryId: number): Promise<Beehive[]> {
    return this.http.get<Beehive[]>(`${this.baseUrl}/apiary/${apiaryId}`);
  }

  // Add this method to your BeehiveService class
  async registerNewHive(form: RegisterHiveForm): Promise<Beehive> {
    return this.http.post<Beehive>(`${this.baseUrl}/register`, form, {
      'Content-Type': 'application/json',
    });
  }

  async checkHiveSerial(form: CheckHiveForm) {
    return this.http.post<Beehive>(`${this.baseUrl}/check`, form, {
      'Content-Type': 'application/json',
    });
  }
}

export type RegisterHiveForm = {
  name: string;
  serial: string;
  apiaryId: number;
};

export type CheckHiveForm = {
  serial: string;
};

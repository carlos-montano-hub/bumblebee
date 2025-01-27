import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NativeHttpService } from '../native-http-client.service';

@Injectable({
  providedIn: 'root',
})
export class MeasureService {
  private baseUrl: string = 'http://localhost:8080/api/measure';

  constructor(private http: NativeHttpService) {}

  // Get a single measure by ID
  async getMeasure(id: number): Promise<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // Create a new measure
  async createMeasure(measure: any): Promise<any> {
    return this.http.post<any>(this.baseUrl, measure, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // Update an existing measure by ID
  async updateMeasure(id: number, measure: any): Promise<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, measure, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // Delete a measure by ID
  async deleteMeasure(id: number): Promise<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Get all measures
  async getMeasures(): Promise<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // Get measures by beehive ID
  async getMeasuresByBeehiveId(beehiveId: number): Promise<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/beehive/${beehiveId}`);
  }
}

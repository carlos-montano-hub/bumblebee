import { Injectable } from '@angular/core';
import { NativeHttpService } from '../native-http-client.service';
import { Apiary } from './apiary.model';

@Injectable({
  providedIn: 'root',
})
export class ApiaryService {
  private baseUrl: string = 'http://localhost:8080/api/apiary';

  constructor(private http: NativeHttpService) {}

  async getAllApiaries(): Promise<Apiary[]> {
    return this.http.get<Apiary[]>(this.baseUrl);
  }

  async getApiaryById(id: number): Promise<Apiary> {
    return this.http.get<Apiary>(`${this.baseUrl}/${id}`);
  }

  async getApiariesByUserId(userId: number): Promise<Apiary[]> {
    return this.http.get<Apiary[]>(`${this.baseUrl}/user/${userId}`);
  }

  async getApiariesByUser(): Promise<Apiary[]> {
    return this.http.get<Apiary[]>(`${this.baseUrl}/user`);
  }

  async createApiary(name: string): Promise<Apiary> {
    return this.http.post<Apiary>(
      `${this.baseUrl}/user`,
      { name },
      {
        'Content-Type': 'application/json',
      }
    );
  }

  async updateApiary(apiary: Apiary): Promise<Apiary> {
    return this.http.put<Apiary>(`${this.baseUrl}/${apiary.id}`, apiary, {
      'Content-Type': 'application/json',
    });
  }

  async deleteApiary(id: number): Promise<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

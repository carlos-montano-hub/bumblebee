import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NativeHttpService } from '../native-http-client.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl: string = 'http://localhost:8080/api/appUsers';

  constructor(private http: NativeHttpService) {}

  // Get a single app user by ID
  async getAppUser(id: number): Promise<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // Create a new app user
  async createAppUser(appUser: any): Promise<any> {
    return this.http.post<any>(this.baseUrl, appUser, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // Update an existing app user by ID
  async updateAppUser(id: number, appUser: any): Promise<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, appUser, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  // Delete an app user by ID
  async deleteAppUser(id: number): Promise<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Get all app users
  async getAppUsers(): Promise<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment.prod';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login-email`, { email, password });
  }

  saveToken(token: string) {
    localStorage.setItem('jwt_token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  logout() {
    localStorage.removeItem('jwt_token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  sendOtp(phone: string) {
    return this.http.post(`${this.apiUrl}/send-otp?phone=${phone}`, {});
  }
  
  verifyOtp(phone: string, otp: string) {
    return this.http.post(`${this.apiUrl}/verify-otp?phone=${phone}&otp=${otp}`, {});
  }
  
  register(user: any) {
    return this.http.post(`${this.apiUrl}/register`, user);
  }
  
}



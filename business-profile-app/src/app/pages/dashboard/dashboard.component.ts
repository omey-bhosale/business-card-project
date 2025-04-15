import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../../environment/environment.prod';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  profiles: any[] = [];
  loading = true;
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.fetchProfiles();
  }

  fetchProfiles() {
    const token = this.auth.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>(`${this.apiUrl}/profiles`, { headers }).subscribe({
      next: (res) => {
        this.profiles = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('❌ Failed to load profiles', err);
        this.loading = false;
      }
    });
  }

  addProfile() {
    this.router.navigate(['/create-profile']);
  }

  viewProfile(id: number) {
    this.router.navigate(['/profile', id]);
  }

  editProfile(id: number) {
    this.router.navigate(['/edit-profile', id]);
  }
}

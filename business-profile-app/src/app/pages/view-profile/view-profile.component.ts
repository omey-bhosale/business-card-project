import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../../environment/environment.prod';

@Component({
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.scss']
})
export class ViewProfileComponent implements OnInit {
  profile: any = null;
  loading: boolean = true;
  error: string = '';
  defaultLogo = 'https://ui-avatars.com/api/?name=Business&background=random';
  currentYear: number = new Date().getFullYear();
  private apiUrl = environment.apiUrl;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) this.fetchProfile(id);
  }

  fetchProfile(id: string) {
    const token = this.auth.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any>(`${this.apiUrl}/profiles/${id}`, { headers }).subscribe({
      next: (res) => {
        this.profile = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading profile', err);
        this.error = 'Profile not found or you are not authorized.';
        this.loading = false;
      }
    });
  }

  get logo(): string {
    return this.profile?.logoUrl?.trim() || this.defaultLogo;
  }

  get whatsappLink(): string {
    const number = this.profile?.whatsapp?.replace(/\D/g, '');
    return number ? `https://wa.me/${number}` : '';
  }

  get googleReviewLink(): string {
    return this.profile?.googleReviewLink || '';
  }

  get appLink(): string {
    return this.profile?.appDownloadLink || '';
  }

  get hasSocialLinks(): boolean {
    return !!(
      this.profile?.facebookUrl ||
      this.profile?.instagramUrl ||
      this.profile?.linkedinUrl ||
      this.profile?.youtubeUrl
    );
  }

  get hasContact(): boolean {
    return !!(this.profile?.phone || this.profile?.email || this.profile?.whatsapp);
  }
}

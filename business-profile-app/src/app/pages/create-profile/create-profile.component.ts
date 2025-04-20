import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { environment } from '../../../environment/environment.prod';

@Component({
  selector: 'app-create-profile',
  templateUrl: './create-profile.component.html'
})
export class CreateProfileComponent {
  profileForm: FormGroup;
  previewData: any = null;
  defaultLogo: string = 'https://via.placeholder.com/100x100.png?text=Logo';
  private apiUrl = environment.apiUrl;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private auth: AuthService,
    private router: Router
  ) {
    this.profileForm = this.fb.group({
      businessName: ['', Validators.required],
      tagline: [''],
      description: ['', Validators.required],
      phone: ['', Validators.required],
      whatsapp: [''],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      googleMapLink: [''],
      googleReviewLink: [''],
      appDownloadLink: [''],
      website: [''],
      facebookUrl: [''],
      instagramUrl: [''],
      linkedinUrl: [''],
      youtubeUrl: [''],
      logoUrl: [''],
      paymentQrUrl: ['']
    });
  }

  onFileSelected(event: any, type: 'logo' | 'qr') {
    const file = event.target.files[0];

    if (!file) return;
    if (!['image/png', 'image/jpeg'].includes(file.type)) {
      alert('Only JPG and PNG files are allowed.');
      return;
    }

    if (file.size > 2 * 1024 * 1024) {
      alert('File size must be under 2MB.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.auth.getToken()}`);

    this.http.post<any>(`${this.apiUrl}/users/upload`, formData, { headers }).subscribe({
      next: (res) => {
        const controlName = type === 'logo' ? 'logoUrl' : 'paymentQrUrl';
        this.profileForm.get(controlName)?.setValue(res.url);
      },
      error: (err) => {
        console.error('Upload failed:', err);
        alert('Upload failed. Check console.');
      }
    });
  }

  preview() {
    if (this.profileForm.valid) {
      this.previewData = this.profileForm.value;
    }
  }

  onSubmit() {
    if (!this.profileForm.valid) return;

    const token = this.auth.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post(`${this.apiUrl}/profiles`, this.profileForm.value, { headers }).subscribe({
      next: () => {
        alert('Profile created!');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Creation failed:', err);
      }
    });
  }
}

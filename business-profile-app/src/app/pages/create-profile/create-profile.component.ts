import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-profile',
  templateUrl: './create-profile.component.html'
})
export class CreateProfileComponent {
  profileForm: FormGroup;
  previewData: any = null;

  constructor(private fb: FormBuilder, private http: HttpClient, private auth: AuthService, private router: Router) {
    this.profileForm = this.fb.group({
      businessName: ['', Validators.required],
      description: ['', Validators.required],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      googleMapLink: [''],
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
    const formData = new FormData();
    formData.append('file', file);

    this.http.post<any>('http://localhost:8080/api/upload', formData).subscribe({
      next: (res) => {
        this.profileForm.get(type === 'logo' ? 'logoUrl' : 'paymentQrUrl')?.setValue(res.url);
      },
      error: (err) => {
        console.error('Upload error:', err);
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

    this.http.post('http://localhost:8080/api/profiles', this.profileForm.value, { headers }).subscribe({
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

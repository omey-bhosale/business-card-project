import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environment/environment.prod';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html'
})
export class SignInComponent {
  signInForm: FormGroup;
  errorMessage = '';
  private apiUrl = environment.apiUrl;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private http: HttpClient
  ) {
    this.signInForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.signInForm.valid) {
      const { email, password } = this.signInForm.value;

      this.http.post<any>(`${this.apiUrl}/login-email`, { email, password }).subscribe({
        next: (res) => {
          this.auth.saveToken(res.token);
          console.log('Login successful:', res);
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Login failed', err);
          this.errorMessage = 'Invalid email or password';
        }
      });
    }
  }
}

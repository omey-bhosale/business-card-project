import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html'
})
export class SignInComponent {
  signInForm: FormGroup;
  errorMessage = '';

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.signInForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.signInForm.valid) {
      const { email, password } = this.signInForm.value;

      this.auth.login(email, password).subscribe({
        next: (res) => {
          this.auth.saveToken(res.token);
          console.log('Login successful:', res);
          this.router.navigate(['/dashboard']); // or /profiles
        },
        error: (err) => {
          console.error('Login failed', err);
          this.errorMessage = 'Invalid email or password';
        }
      });
    }
  }
}

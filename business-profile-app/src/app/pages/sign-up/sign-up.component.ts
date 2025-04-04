import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent {
  signUpForm: FormGroup;
  otpSent = false;
  otpVerified = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.signUpForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      password: ['', Validators.required],
      otp: ['']
    });
  }

  sendOTP() {
    const phone = this.signUpForm.get('phone')?.value;
    if (!phone) return;

    this.auth.sendOtp(phone).subscribe({
      next: () => {
        this.otpSent = true;
        this.successMessage = 'OTP sent to your phone!';
        this.errorMessage = '';
        console.log('OTP sent');
      },
      error: (err) => {
        this.errorMessage = 'Failed to send OTP';
        this.successMessage = '';
        console.error(err);
      }
    });
  }

  verifyOTP() {
    const phone = this.signUpForm.get('phone')?.value;
    const otp = this.signUpForm.get('otp')?.value;

    if (!otp) {
      this.errorMessage = 'Please enter the OTP';
      return;
    }

    this.auth.verifyOtp(phone, otp).subscribe({
      next: (res: any) => {
        if (res.verified === true || res === true) {
          this.otpVerified = true;
          this.successMessage = 'OTP verified!';
          this.errorMessage = '';
          console.log('OTP verified');
        } else {
          this.errorMessage = 'Invalid OTP';
          this.successMessage = '';
        }
      },
      error: (err) => {
        this.errorMessage = 'OTP verification failed';
        this.successMessage = '';
        console.error(err);
      }
    });
  }

  onSubmit() {
    if (!this.otpVerified) {
      this.errorMessage = 'Please verify OTP before registering';
      return;
    }

    if (this.signUpForm.valid) {
      const { name, email, phone, password } = this.signUpForm.value;
      const payload = { name, email, phone, password };

      this.auth.register(payload).subscribe({
        next: (res: any) => {
          this.auth.saveToken(res.token);
          this.successMessage = 'Registration successful!';
          this.errorMessage = '';
                      this.router.navigate(['/dashboard']); // route after login
                    },
                    error: (err) => {
                      this.errorMessage = 'Registration failed';
                      this.successMessage = '';
                      console.error(err);
                    }
                  });
                }
              }
            }

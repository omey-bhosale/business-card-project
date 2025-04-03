// sign-up.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent {
  signUpForm: FormGroup;

  constructor(private fb: FormBuilder) {
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
    console.log('Sending OTP to:', phone);
    // Call backend API to send OTP
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      console.log('Sign-Up Data:', this.signUpForm.value);
      // Submit to backend for OTP verification & account creation
    }
  }
}

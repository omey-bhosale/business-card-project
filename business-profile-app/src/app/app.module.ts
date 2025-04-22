import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignUpComponent } from './pages/sign-up/sign-up.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SignInComponent } from './pages/sign-in/sign-in.component';
import { HttpClientModule } from '@angular/common/http';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CreateProfileComponent } from './pages/create-profile/create-profile.component';
import { ViewProfileComponent } from './pages/view-profile/view-profile.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { FadeInOnScrollDirective } from './landing-page/fade-in-on-scroll.directive';

@NgModule({
  declarations: [
    AppComponent,
    SignUpComponent,
    SignInComponent,
    DashboardComponent,
    CreateProfileComponent,
    ViewProfileComponent,
    LandingPageComponent,
    FadeInOnScrollDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

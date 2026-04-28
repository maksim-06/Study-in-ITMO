import { bootstrapApplication } from '@angular/platform-browser';
import {provideRouter, withHashLocation} from '@angular/router';
import {AppComponent} from './app/app';
import {Login} from './app/login/login';
import {Main} from './app/main/main';


bootstrapApplication(AppComponent, {
  providers: [
    provideRouter([
      { path: '', redirectTo: '/login', pathMatch: 'full' },
      { path: 'login', component: Login },
      { path: 'main', component: Main }
    ], withHashLocation())
  ]
}).catch(err => console.error(err));

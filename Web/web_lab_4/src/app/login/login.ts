import {ChangeDetectorRef, Component} from '@angular/core';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {Auth} from '../services/auth';


@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  login: string = "";
  password: string = "";
  errorMessage: string = "";

  constructor(
    private auth: Auth,
    private router: Router,
    private cdr: ChangeDetectorRef) {
  }

  onLogin() {
    if (!this.validate()) return;

    this.errorMessage = "";

    this.auth.login(this.login, this.password).subscribe({
      next: () => {
        this.router.navigate(["/main"]);
      }, error: (error) => {
        this.errorMessage = error.error.message;
        this.cdr.detectChanges();
      }
    });
  }


  onRegister() {
    if (!this.validate()) return;

    this.errorMessage = "";

    this.auth.register(this.login, this.password).subscribe({
      next: () => {
        this.router.navigate(["/main"]);
      }, error: (error: any) => {
        this.errorMessage = error.error.message;
        this.cdr.detectChanges();
      }
    });
  }

  private validate(): boolean {
    if (!this.login.trim()) {
      this.errorMessage = "Enter your login";
      return false;
    }

    if (!this.password.trim()) {
      this.errorMessage = "Enter your password";
      return false;
    }
    return true;
  }
}

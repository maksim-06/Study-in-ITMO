import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


@Injectable({
  providedIn: 'root',
})
export class Auth {
  private url = "/web_lab_4/api/auth";

  constructor(
    private http: HttpClient) {
  }


  register(login: string, password: string) {

    return this.http.post(`${this.url}/register`, {
      login,
      password
    });
  }

  login(login: string, password: string) {

    return this.http.post(`${this.url}/login`, {
      login,
      password
    });
  }

  logout() {
    return this.http.post(`${this.url}/logout`, {});
  }

}

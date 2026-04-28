import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Point {
  private url = "/web_lab_4/api/points";

  constructor(private http: HttpClient) {
  }

  checkPoint(x: string, y: string, r: string) {
    const params = {x, y, r}
    return this.http.post(`${this.url}/check`, params);
  }

  getUserPoints() {
    return this.http.get<any[]>(`${this.url}/my-points`);
  }
}

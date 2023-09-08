import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, retry, throwError } from 'rxjs';
import { SnackBarErrorComponent } from './snack-bar-error/snack-bar-error.component';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  private apiPath = 'http://localhost:8080/';

  private loggedSubject = new BehaviorSubject<string | null>(null);
  public logged$ = this.loggedSubject.asObservable();

  constructor(private http: HttpClient, private router: Router, private snackbar: MatSnackBar) { }

  isAuthenticated() {
    return this.loggedSubject.value;
  }

  authenticate(username: string, password: string) {
    if (!username || !password) {
      return;
    }
    
    let credentials = btoa(username + ':' + password);

    let headers = new HttpHeaders({ authorization: 'Basic ' + credentials });
    headers = headers.append("X-Requested-With", "XMLHttpRequest");

    this.http.post(this.apiPath + 'process_login', {}, { headers: headers }).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        if (e.status === 0) {
          this.snackbar.openFromComponent(SnackBarErrorComponent, { duration: 3000, data: 'Something went wrong' });
        } else {
          this.snackbar.openFromComponent(SnackBarErrorComponent, { duration: 3000, data: 'Invalid Credentials' });
        }
        return throwError(() => new Error('Something went wrong'));
      }),
    ).subscribe(response => {
      this.loggedSubject.next(username);
      this.router.navigate(['/home']);
    })
  }

  logout() {
    this.http.post(this.apiPath + 'logout', {}).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        this.snackbar.openFromComponent(SnackBarErrorComponent, { duration: 3000, data: 'Something went wrong' });
        return throwError(() => new Error('Something went wrong'));
      }),
    ).subscribe(response => {
      this.loggedSubject.next(null);
      this.router.navigate(['/login']);
    });
  }

  register(username: string, password: string) {
    this.http.post(this.apiPath + 'process_register', {username: username, password: password}).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        if (e.status === 409) {
          this.snackbar.openFromComponent(SnackBarErrorComponent, { duration: 3000, data: 'Username already exists' });
        } else {
          this.snackbar.openFromComponent(SnackBarErrorComponent, { duration: 3000, data: 'Invalid Credentials' });
        }
        return throwError(() => new Error('Something went wrong'));
      }),
    ).subscribe(r => this.authenticate(username, password));
  }
}

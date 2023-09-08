import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpXsrfTokenExtractor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private tokenExtractor: HttpXsrfTokenExtractor) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let token = this.tokenExtractor.getToken() as string;
    
    if (token !== null && !request.headers.has('X-XSRF-TOKEN')) {
      request = request.clone({ headers: request.headers.set('X-XSRF-TOKEN', token) });
    }
    request = request.clone({ withCredentials: true });
    
    return next.handle(request);
  }
}

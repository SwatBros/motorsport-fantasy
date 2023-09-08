import { CanActivateFn, Router } from '@angular/router';
import { AppService } from '../shared/app.service';
import { inject } from '@angular/core';


export const authGuard: CanActivateFn = (route, state) => {
  if (inject(AppService).isAuthenticated()) {
    return true;
  }
  
  inject(Router).navigate(['/login']);
  return false;
};

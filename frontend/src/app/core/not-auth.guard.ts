import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AppService } from '../shared/app.service';

export const notAuthGuard: CanActivateFn = (route, state) => {
  if (!inject(AppService).isAuthenticated()) {
    return true;
  }

  inject(Router).navigate(['/home']);
  return false;
};

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { FormControl, FormGroup, FormGroupDirective, NgForm, ReactiveFormsModule, Validators } from '@angular/forms';
import { UnauthorizedPageComponent } from '../../shared/unauthorized-page/unauthorized-page.component';
import { passwordValidator } from '../password-validator.directive';
import { ErrorStateMatcher } from '@angular/material/core';
import { AppService } from '../../shared/app.service';

export class PasswordStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && (control.invalid || form?.hasError('passwordMismatch')) && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule, MatInputModule, MatButtonModule, MatCardModule, MatIconModule, ReactiveFormsModule,
    UnauthorizedPageComponent
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent {
  hidePassword = true;
  hideConfirm = true;

  matcher = new PasswordStateMatcher();

  registerForm = new FormGroup({
    username: new FormControl('', { validators: Validators.required, nonNullable: true }),
    password: new FormControl('', { validators: Validators.required, nonNullable: true }),
    confirm: new FormControl('', { validators: Validators.required, nonNullable: true })
  }, {validators: passwordValidator});

  constructor(private app: AppService) {}

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  toggleConfirmVisibility() {
    this.hideConfirm = !this.hideConfirm;
  }

  getConfirmErrorMessage() {
    if (this.registerForm.controls.confirm.hasError('required')) {
      return 'Invalid Password';
    }

    if (this.registerForm.controls.confirm.hasError('passwordMismatch')) {
      return 'Passwords must match';
    }

    return '';
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    let username = this.registerForm.value.username;
    let password = this.registerForm.value.password;
    let confirm = this.registerForm.value.confirm;

    if (!username || !password || !confirm || password !== confirm) {
      return;
    }

    this.app.register(username, password);
  }
}

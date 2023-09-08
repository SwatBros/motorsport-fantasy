import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UnauthorizedPageComponent } from '../shared/unauthorized-page/unauthorized-page.component';
import { AppService } from '../shared/app.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule, MatInputModule, MatButtonModule, MatCardModule, MatIconModule, ReactiveFormsModule,
    UnauthorizedPageComponent
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent {
  hide = true;

  constructor(private app: AppService) {}

  loginForm = new FormGroup({
    username: new FormControl('', { validators: Validators.required, nonNullable: true }),
    password: new FormControl('', { validators: Validators.required, nonNullable: true }),
  });

  toggleVisibility() {
    this.hide = !this.hide;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    let username = this.loginForm.value.username;
    let password = this.loginForm.value.password;

    if (!username || !password) {
      return;
    }

    this.app.authenticate(username, password);
  }
}

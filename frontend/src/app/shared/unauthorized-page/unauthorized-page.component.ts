import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { Router, RouterLink } from '@angular/router';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-unauthorized-page',
  standalone: true,
  imports: [
    CommonModule, MatSidenavModule,
    MatToolbarModule, MatIconModule, MatButtonModule,
    RouterLink, MatListModule, MatDividerModule
  ],
  templateUrl: './unauthorized-page.component.html',
  styleUrls: ['./unauthorized-page.component.sass']
})
export class UnauthorizedPageComponent {
  isSidenavOpen = false;

  constructor(public router: Router) {}

  toggleSidenav() {
    this.isSidenavOpen = !this.isSidenavOpen;
  }
}

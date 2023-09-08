import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { AppService } from '../app.service';
import { MatDividerModule } from '@angular/material/divider';
import { ApiService } from '../../leagues/shared/api.service';
import { RouterLink } from '@angular/router';
import { combineLatest, map, mergeMap, of } from 'rxjs';

@Component({
  selector: 'app-authorized-page',
  standalone: true,
  imports: [
    CommonModule, MatSidenavModule,
    MatToolbarModule, MatIconModule, MatButtonModule,
    MatMenuModule, MatListModule, MatDividerModule, RouterLink
  ],
  templateUrl: './authorized-page.component.html',
  styleUrls: ['./authorized-page.component.sass']
})
export class AuthorizedPageComponent {
  isSidenavOpen = true;

  leagues: string[] = []

  constructor(public app: AppService, public api: ApiService) {}

  toggleSidenav() {
    this.isSidenavOpen = !this.isSidenavOpen;
  }

  ngOnInit() {
    this.api.getAllLeagues().subscribe();
  }

  getCurrentPlayer() {
    return this.api.currentLeague$.pipe(
      mergeMap(league => combineLatest([of(league), this.app.logged$])),
      map(c => c[0]?.players.find(p => p.name === c[1])));
  }
}

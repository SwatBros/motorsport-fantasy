import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LeagueNameComponent } from '../shared/league-name/league-name.component';
import { AuthorizedPageComponent } from 'src/app/shared/authorized-page/authorized-page.component';
import { ApiService } from '../shared/api.service';

@Component({
  selector: 'app-create-league',
  standalone: true,
  imports: [CommonModule, LeagueNameComponent, AuthorizedPageComponent],
  templateUrl: './create-league.component.html',
  styleUrls: ['./create-league.component.sass']
})
export class CreateLeagueComponent {
  constructor(private api: ApiService) {}

  onSubmit(leagueName: string) {
    this.api.createLeague(leagueName).subscribe();
  }
}

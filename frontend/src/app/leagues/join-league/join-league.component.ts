import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizedPageComponent } from 'src/app/shared/authorized-page/authorized-page.component';
import { ApiService } from '../shared/api.service';
import { LeagueNameComponent } from '../shared/league-name/league-name.component';

@Component({
  selector: 'app-join-league',
  standalone: true,
  imports: [LeagueNameComponent, CommonModule, AuthorizedPageComponent],
  templateUrl: './join-league.component.html',
  styleUrls: ['./join-league.component.sass']
})
export class JoinLeagueComponent {
  constructor(private api: ApiService) {}

  onSubmit(leagueName: string) {
    this.api.joinLeague(leagueName).subscribe();
  }
}

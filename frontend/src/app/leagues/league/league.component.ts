import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizedPageComponent } from '../../shared/authorized-page/authorized-page.component';
import { ApiService } from '../shared/api.service';
import { MatChipOption, MatChipsModule } from '@angular/material/chips';
import { catchError, map } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { AppService } from '../../shared/app.service';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-league',
  standalone: true,
  imports: [CommonModule, AuthorizedPageComponent, MatChipsModule, MatCardModule, MatButtonModule, MatDividerModule],
  templateUrl: './league.component.html',
  styleUrls: ['./league.component.sass']
})
export class LeagueComponent {
  constructor(public api: ApiService, public app: AppService) {}

  isSelected(name: string) {
    return this.api.currentLeague$.pipe(map(v => v?.series.find(s => s.name === name) ? true : false));
  }

  chipClick(series: string, chip: MatChipOption) {
    if (chip.selected) {
      this.api.addLeagueSeries(series).pipe(
        catchError(e => {
          console.log(e);
          //chip.deselect();
          throw(e);
        })).subscribe(() => this.api.getAllLeagues().subscribe());
    } else {
      this.api.removeLeagueSeries(series).pipe(
        catchError(e => {
         // chip.select();
          throw(e);
        })).subscribe(() => this.api.getAllLeagues().subscribe());
    }
  }
}

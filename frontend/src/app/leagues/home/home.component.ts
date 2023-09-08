import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizedPageComponent } from '../../shared/authorized-page/authorized-page.component';
import { ApiService } from '../shared/api.service';
import { MatTableModule } from '@angular/material/table';
import { MatExpansionModule } from '@angular/material/expansion';
import { AppService } from 'src/app/shared/app.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, AuthorizedPageComponent, MatTableModule, MatExpansionModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent {
  displayedColumns = ['position', 'name', 'points'];

  constructor(public api: ApiService, public app: AppService) {}

  ngOnInit() {
    this.api.getAllLeagues().subscribe();
  }
}

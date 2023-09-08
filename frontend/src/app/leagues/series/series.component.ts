import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../shared/api.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-series',
  standalone: true,
  imports: [CommonModule, MatButtonModule, RouterLink],
  templateUrl: './series.component.html',
  styleUrls: ['./series.component.sass']
})
export class SeriesComponent {
  constructor(public api: ApiService) {}
}

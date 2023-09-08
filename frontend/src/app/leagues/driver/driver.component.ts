import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from 'src/app/leagues/shared/api.service';
import { ActivatedRoute } from '@angular/router';
import { combineLatest, first, map, mergeMap, of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-driver',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, NgChartsModule],
  templateUrl: './driver.component.html',
  styleUrls: ['./driver.component.sass']
})
export class DriverComponent {
  private series!: string;

  public chartData: ChartConfiguration<'line'>['data'] = {
    datasets: [{
      data: [],
      label: 'Points',
      xAxisID: 'xAxis',
      yAxisID: 'yAxis',
    }],
    labels: []
  }

  public chartOptions: ChartConfiguration<'line'>['options'] = {
    scales: {
      xAxis: {
        grid: { display: false }
      },
      yAxis: {
        grid: { display: false },
        suggestedMin: 0,
        suggestedMax: 34,
      }
    }
  }

  constructor(private api: ApiService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.getDriver().subscribe( driver => {
      this.chartData.datasets[0].data = driver?.results?.map(v => v.points)!;
      this.chartData.labels = driver?.results?.map(v => [v.name])!;
    });
  }

  getDriver() {
    return this.route.paramMap.pipe(
      mergeMap(params => combineLatest([of(params), this.api.currentLeague$])),
      map(c => {
        this.series = c[0]?.get('series')!;
        let driver = c[1]?.series.find(p => p.name === c[0]?.get('series'))?.drivers.find(p => p.name === c[0]?.get('driver'));
        driver?.results.sort((a, b) => a.index - b.index);
        return driver;
      })
    );
  }

  createContract() {
    this.getDriver().pipe(
      first()
    ).subscribe(d => {
        d && this.api.createContract(this.series, d.name);
    });
  }

  deleteContract() {
    this.getDriver().pipe(first()).subscribe(d => {
        d && this.api.deleteContract(this.series, d.name).subscribe(() => 
        this.api.getAllLeagues().subscribe());
    });
  }

  getContract() {
    return this.api.getUserPlayer().pipe(
      mergeMap(p => combineLatest([of(p), this.getDriver()])),
      map(c => {
        return c[0]?.contracts.find(ct => ct.driverName === c[1]?.name);
      })
    )
  }
}

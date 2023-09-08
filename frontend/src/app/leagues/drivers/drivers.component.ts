import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ApiService } from 'src/app/leagues/shared/api.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { combineLatest, map, mergeMap, of } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { Driver } from '../shared/driver';

@Component({
  selector: 'app-drivers',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatIconModule, MatSortModule, MatInputModule, RouterLink],
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.sass']
})
export class DriversComponent {
  public displayedColumns = ['position', 'image', 'name', 'contract', 'value', 'trend', 'points'];
  public t = .25;

  public series: string = '';

  dataSource: MatTableDataSource<Driver>;

  //TODO: sort by contract
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public api: ApiService, private route: ActivatedRoute) {
    this.dataSource = new MatTableDataSource<Driver>([]);
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }

  ngOnInit() {
    this.getDrivers().subscribe(v => this.dataSource.data = v);
  }

  getDrivers() {
    return this.route.paramMap.pipe(
      mergeMap(params => combineLatest([of(params), this.api.currentLeague$])),
      map(c => {
        this.series = c[0]?.get('series')!;
        return c[1]?.series.find(p => p.name === c[0]?.get('series'))?.drivers!;
      })
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  hasContract(driver: string) {
    return this.api.getUserPlayer().pipe(
      map(c => {
        return c?.contracts.find(ct => ct.driverName === driver);
      })
    )
  }
}
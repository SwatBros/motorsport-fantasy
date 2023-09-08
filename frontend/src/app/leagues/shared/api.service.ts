import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, combineLatest, map, mergeMap, of, retry, tap } from 'rxjs';
import { League } from './league';
import { AppService } from 'src/app/shared/app.service';
import { Standings } from './standings';
import { SnackBarErrorComponent } from 'src/app/shared/snack-bar-error/snack-bar-error.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private path = 'http://localhost:8080/'

  private leagueSubject = new BehaviorSubject<League[]>([]);
  public leagues$ = this.leagueSubject.asObservable();

  private currentLeagueSubject = new BehaviorSubject<League | null>(null);
  public currentLeague$ = this.currentLeagueSubject.asObservable();

  private standingsSubject = new BehaviorSubject<Standings | null>(null);
  public standings$ = this.standingsSubject.asObservable();

  constructor(private http: HttpClient, private app: AppService, private snackbar: MatSnackBar, private router: Router) {
    this.app.logged$.subscribe(v => {
      if (!v) {
        this.leagueSubject.next([]);
        this.currentLeagueSubject.next(null);
        this.standingsSubject.next(null);
      }
    });
  }

  getAllLeagues() {
    return this.http.get<League[]>(this.path + 'leagues').pipe(
      tap(
        response => {
          console.log(response);
          response.sort((l, m) => l.name.localeCompare(m.name)).forEach(l => {
            l.players.sort((a, b) => b.points - a.points);
            l.series.forEach(s => s.drivers.sort((a, b) => a.position - b.position))
          });
          this.leagueSubject.next(response);

          if (this.currentLeagueSubject.value === null) {
            this.setCurrentLeague(response[0]);
          } else {
            let league = response.find(l => l.name === this.currentLeagueSubject.value?.name);
            league && this.setCurrentLeague(league);
          }

          this.getStandings();
        }
      )
    );
  }

  setCurrentLeague(league: League | null | string) {
    if (typeof league === 'string') {
      league = this.leagueSubject.value?.find(l => l.name === league) || null;
    }
    this.currentLeagueSubject.next(league);
    this.standingsSubject.next(null);
    this.getStandings();
  }

  getUserPlayer() {
    return this.currentLeague$.pipe(
      mergeMap(l => combineLatest([of(l), this.app.logged$])),
      map(c => {
        return c[0]?.players.find(p => p.name === c[1]);
      })
    );
  }

  createLeague(leagueName: string) {
    return this.http.post(this.path + 'leagues', leagueName).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        if (e.status === 409) {
          this.openSnackBar('A league with this name already exists');
        } else {
          this.openSnackBar();
        }
        
        throw(e);
      }),
      tap(() => {
        this.getAllLeagues().subscribe(() => this.setCurrentLeague(leagueName));
        //TODO: probably move navigation outside the service
        this.router.navigate(['/home']);
      })
    )
  }

  createContract(series: string, driver: string) {
    return this.http.post(this.path + 'leagues/' + this.currentLeagueSubject.value?.name + '/series/' + series + '/drivers/' + driver + '/contracts', {})
      .pipe(
        retry(2),
        catchError((e : HttpErrorResponse) => {
          if (e.status === 403) {
            this.openSnackBar('Not enough money');
          } else {
            this.openSnackBar();
          }
          throw(e);
        })
      ).subscribe(() => this.getAllLeagues().subscribe());
  }

  deleteContract(series: string, driver: string) {
    return this.http.delete(this.path + 'leagues/' + this.currentLeagueSubject.value?.name + '/series/' + series + '/drivers/' + driver + '/contracts', {})
      .pipe(retry(2));
  }

  getStandings() {
    this.http.get<Standings>(this.path + 'leagues/' + this.currentLeagueSubject.value?.name + '/standings')
      .subscribe(v => {
        Object.keys(v).forEach(k => {
          v[k].sort((a, b) => b.points - a.points);
        })

        this.standingsSubject.next(v);
      });
  }

  joinLeague(leagueName: string) {
    return this.http.post(this.path + 'leagues/' + leagueName + '/participants', {}).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        if (e.status === 404) {
          this.openSnackBar('League not found');
        } else {
          this.openSnackBar();
        }
        throw(e);
      }),
      tap(() => {
        this.getAllLeagues().subscribe(() => this.setCurrentLeague(leagueName));
        this.router.navigate(['/home']);
      })
    );
  }

  getSeries() {
    return this.http.get<string[]>(this.path + 'series');
  }

  leaveLeague() {
    this.http.delete(this.path + 'leagues/' + this.currentLeagueSubject.value?.name + '/participants', {}).pipe(
      catchError((e: HttpErrorResponse) => {
        if (e.status === 404) {
          this.openSnackBar('League not found');
        } else if (e.status === 405) {
          this.openSnackBar('Owner cannot leave the league');
        } else {
          this.openSnackBar();
        }
        throw(e);
      })
    ).subscribe(() => {
      this.setCurrentLeague(null);
      this.getAllLeagues().subscribe();
    });
  }

  addLeagueSeries(series: string) {
    return this.http.post(this.path + 'leagues/' + this.currentLeagueSubject.value?.name + '/series', series).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        this.openSnackBar();
        throw(e);
      })
    );
  }

  removeLeagueSeries(series: string) {
    return this.http.delete(this.path + 'leagues/' + this.currentLeagueSubject.value?.name + '/series/' + series).pipe(
      retry(2),
      catchError((e: HttpErrorResponse) => {
        this.openSnackBar();
        throw(e);
      })
    );
  }

  openSnackBar(msg: string = 'Something went wrong') {
    this.snackbar.openFromComponent(SnackBarErrorComponent, { duration: 3000, data: msg });
  }
}

import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './registration/register/register.component';
import { notAuthGuard } from './core/not-auth.guard';
import { authGuard } from './core/auth.guard';
import { HomeComponent } from './leagues/home/home.component';
import { ManageComponent } from './leagues/manage/manage.component';
import { DriversComponent } from './leagues/drivers/drivers.component';
import { SeriesComponent } from './leagues/series/series.component';
import { DriverComponent } from './leagues/driver/driver.component';
import { JoinLeagueComponent } from './leagues/join-league/join-league.component';
import { LeagueComponent } from './leagues/league/league.component';
import { CreateLeagueComponent } from './leagues/create-league/create-league.component';

export const routes: Routes = [
	{ path: 'login', component: LoginComponent, canActivate: [notAuthGuard] },
	{ path: 'register', component: RegisterComponent, canActivate: [notAuthGuard] },
	{ path: 'home', component: HomeComponent, canActivate: [authGuard] },
	{ path: 'manage', component: ManageComponent, canActivate: [authGuard], children: [
		{ path: ':series', component: DriversComponent },
		{ path: ':series/:driver', component: DriverComponent },
		{ path: '', component: SeriesComponent }
	]},
	{ path: 'create', component: CreateLeagueComponent, canActivate: [authGuard] },
	{ path: 'join', component: JoinLeagueComponent, canActivate: [authGuard] },
	{ path: 'league', component: LeagueComponent, canActivate: [authGuard] },
	{ path: '', redirectTo: 'home', pathMatch: 'full' }
];

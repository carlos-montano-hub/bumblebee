import { Routes } from '@angular/router';
import { AddApiaryComponent } from './pages/add-apiary/add-apiary.component';
import { AddHiveComponent } from './pages/add-hive/add-hive.component';
import { AddMenuComponent } from './pages/add-menu/add-menu.component';
import { HiveDetailsComponent } from './pages/hive-details/hive-details.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';
import { StatusComponent } from './pages/status/status.component';
import { AuthGuardService } from './services/auth/auth-guard.service';

export const routes: Routes = [
  {
    path: 'status',
    component: StatusComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'add',
    component: AddMenuComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'add-hive',
    component: AddHiveComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'add-apiary',
    component: AddApiaryComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'hive-details/:id',
    component: HiveDetailsComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'login',
    component: LoginPageComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: '**',
    component: HomePageComponent,
    canActivate: [AuthGuardService],
  },
];

import { ModuleWithProviders } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BattleComponent } from './battle/battle.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';

const routes: Routes = [
    {path: 'battle', component: BattleComponent },
    { path: 'login', component: LoginComponent },
    { path: 'logout', component: LogoutComponent },
    { path: '**', redirectTo: '/battle', pathMatch: 'full' },
    { path: '',  redirectTo: '/battle', pathMatch: 'full' }, // catch all route

];
export const routingModule: ModuleWithProviders = RouterModule.forRoot(routes);

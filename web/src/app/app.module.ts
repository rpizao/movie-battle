import { APP_BASE_HREF, CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { NgbAccordionModule, NgbModule, NgbToast } from '@ng-bootstrap/ng-bootstrap';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { AppComponent } from './app.component';
import { routingModule } from './app.routing';
import { BattleComponent } from './battle/battle.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { AuthGuard } from './_auth/guards/auth.guard';
import { AuthService } from './_auth/services/auth.service';
import { TokenIntercept } from './_auth/tokenintercept';
import { AlertModule } from './_shared/components/alert/alert.module';
import { LoadingComponent } from './_shared/components/loading/loading.components';
import { LoadingService } from './_shared/components/loading/loading.service';
import { MessageDialogService } from './_shared/components/message-dialog/confirm-dialog.service';
import { MessageDialogComponent } from './_shared/components/message-dialog/message-dialog.component';
import { UiModule } from './_shared/ui/ui.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LogoutComponent,
    MessageDialogComponent,
    LoadingComponent,
    BattleComponent,
  ],
  imports: [
    CommonModule,
    UiModule,
    HttpClientModule,
    RouterModule,
    routingModule,
    ReactiveFormsModule,
    FormsModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    AlertModule,
    NgbModule,
    NgbAccordionModule
  ],
  providers: [
    { provide: APP_BASE_HREF, useValue: '/'},
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenIntercept,
      multi: true
    },
    AuthService,
    MessageDialogService,
    LoadingService,
    NgbToast
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Subscription } from 'rxjs';
import { AuthService } from './_auth/services/auth.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  loggedIn$: BehaviorSubject<boolean>;
  private isLoggedIn_subscription: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    @Inject(DOCUMENT) private document: any
  ) { }

  ngOnInit() {

    this.loggedIn$  =  this.authService.isLoggedIn;

    this.isLoggedIn_subscription  = this.authService.isLoggedIn.subscribe(
      (status) => {
        this.setBodyClassName(status);

        if (status) {
          this.handleLoginSuccess();
        } else {
          this.handleLoginError();
        }
      }
    );

    if (this.authService.hasToken()) {
      this.logout();
    }

  }

  ngOnDestroy() {
    if (this.isLoggedIn_subscription)  { this.isLoggedIn_subscription.unsubscribe(); }
  }

  private setBodyClassName(status: boolean) {
    this.document.body.className = status ? '' : 'publicPage';
  }

  private handleLoginError() {
    this.router.navigate(['/login']);
  }

  private handleLoginSuccess() {
    // console.log('** handleLoginSuccess **');
  }

  private logout() {
    this.authService.logout();
  }

}

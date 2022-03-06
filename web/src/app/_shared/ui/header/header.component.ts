import { Component, OnInit } from '@angular/core';
import { UserModel } from 'src/app/_auth/models/user.model';
import { BattleService } from 'src/services/battle.service';
import { AuthService } from './../../../_auth/services/auth.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  navToggle: Boolean = false;
  navSelectedPosition: number = 0;
  user: UserModel;
  private _state: string[];
  private _robotLive: boolean;

  constructor(
    private authService: AuthService,
    private statsService: BattleService
  ) {
    this.user = this.authService.getUserData();
  }

  ngOnInit() {
  }

  logout() {
    this.authService.logout();
  }

  toggleNav() {
    this.navToggle  = !this.navToggle;
  }

  isSelected(position: number): boolean {
    return position == this.navSelectedPosition;
  }


}

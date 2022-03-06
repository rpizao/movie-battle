import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, of, Subject } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { UserModel } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLoggedIn = new BehaviorSubject(false);

  onLogin  = new Subject<any>(); // deprecated
  onLogout  = new Subject<any>(); // deprecated

  private token: string  = null;
  private userData: UserModel = null;

  constructor(
    private http: HttpClient,
  ) {
    // try and find out if there was a localstorage token was set
    this.resolveToken();
  }

  // check if localstorage token was set
  // if so, set the token in the service
  // and set the login status
  resolveToken(): boolean {
    this.token = localStorage.getItem('token');
    this.isLoggedIn.next(this.token ?  true : false);
    return this.token ? true : false;
  }

  getToken(): string {
    return this.token;
  }

  hasToken(): boolean  {
    return this.getToken() ? true : false;
  }

  async logout() {
    return this.http.get(environment['apiBaseUrl'] + '/auth/logout').toPromise().then(
      () => {
        // clear any current data
        this.clearData();

        // tell the rest of the application about the logout
        this.isLoggedIn.next(false);
        return true;
      },
      (err) => {
        return false;
      }
    );
  }

  async login({ username , password }): Promise<any>  {
    this.clearData();

    const loginData  = {
      'username' : username,
      'password' : password
    };

    const accept  = await this.http.post(environment['apiBaseUrl'] + '/auth/login' , loginData).toPromise();

    if (accept) {

        this.setDataAfterLogin(accept);
        this.isLoggedIn.next(true);
        return accept['user'];

    } else {
      return false;
    }
  }

  clearData() {
    this.userData  = null;
    this.token  = null;
    localStorage.clear();
  }

  getUserData(): UserModel {
    return JSON.parse(localStorage.getItem("usermeta")) as UserModel;
  }

  private setDataAfterLogin(data) {
    this.token  = data['token'];

    // store some user data in the service
    this.userData  = data['user'];

    // store some data in local storage (webbrowser)
    localStorage.setItem('token' , this.token);
    localStorage.setItem('usermeta' , JSON.stringify(this.userData));
  }
}

import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';



import { AuthService } from './services/auth.service';

@Injectable()
export class TokenIntercept implements HttpInterceptor {

    constructor(
        private authService: AuthService,
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token    = this.authService.getToken();
        const headers    = {};
        headers['Access-Control-Allow-Origin'] = '*';
        /* headers["Access-Control-Expose-Headers"] = "Content-Length, X-JSON";
        headers["Access-Control-Allow-Methods"] = "GET, POST";
        headers["Access-Control-Allow-Headers"] = "*";

        if (token !== null) {
            headers['Authorization'] = 'Bearer ' + token;
            headers['Access-Control-Allow-Origin'] = '*';
            headers['Access-Control-Allow-Methods'] = "GET, POST, PATCH, PUT, DELETE, OPTIONS";
            headers['Access-Control-Allow-Headers'] = 'Origin, Content-Type, X-Auth-Token';
        } */

        const modified = request.clone(
            {
                setHeaders: headers,
            }
        );
        return next.handle(modified);
    }
}

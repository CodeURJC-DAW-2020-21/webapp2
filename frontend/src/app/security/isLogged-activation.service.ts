import {Injectable} from '@angular/core';
import {CanActivate, Router} from "@angular/router";
import {LoggedInUserService} from "../logged-in-user.service";
import {Observable, of} from "rxjs";
import {User} from "../user/user.model";
import {map} from "rxjs/operators";
import {catchError} from "rxjs/internal/operators/catchError";

@Injectable()
export class IsLoggedActivationService implements CanActivate {

    constructor(private loggedUser: LoggedInUserService, private router: Router) {
    }

    canActivate(): Observable<boolean> {
        return this.loggedUser.getLoggedUser().pipe(map((response: User) => {
            if (response !== undefined && response.licenseId !== undefined) {
                return true;
            } else {
                this.router.navigate(['/403']);
                return false;
            }
        }), catchError((error) => {
            this.router.navigate(['/403']);
            return of(false);
        })) as Observable<boolean>;
    }
}

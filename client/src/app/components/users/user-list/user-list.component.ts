import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/_services/user.service';
import { Router } from '@angular/router';
import { Observable } from "rxjs";
import { User } from 'src/app/models/User';
import { TokenStorageService } from 'src/app/_services/token-storage.service';



@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit{

  users: Observable<User[]> | undefined;
  isLoggedIn = false;
  showAdminBoard = false;
  showUserBoard = false;
  private role:string;

  constructor(private userService: UserService, private router: Router, private tokenStorageService: TokenStorageService ){}

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.role
       = user.role;

      this.showAdminBoard = this.role.includes('ROLE_ADMIN');
      this.showUserBoard = this.role.includes('ROLE_USER');
    }
    this.reloadData();
  }

  reloadData() {
    this.users = this.userService.getUsersList();
  }

  deleteUser(publicId: string) {
    this.userService.deleteUser(publicId)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  updateUser(publicId: string){
    this.router.navigate(['update-user', publicId]);
  }

}

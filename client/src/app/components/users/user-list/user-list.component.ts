import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/_services/user.service';
import { Router } from '@angular/router';
import { Observable } from "rxjs";
import { User } from 'src/app/models/User';



@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit{

  users: Observable<User[]> | undefined;

  constructor(private userService: UserService, private router: Router){}

  ngOnInit(): void {
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

import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/_services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from "rxjs";
import { User } from 'src/app/models/User';

@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrls: ['./user-update.component.css']
})
export class UserUpdateComponent  implements OnInit{

  publicId: string ;
  user : User ;

  constructor(private route: ActivatedRoute,private router: Router,
    private userService: UserService) { 
      this.user = {
        firstName : '',
        lastName: '',
        email: '',
        password: '',
        publicId: '',
        role: ''
      }
    }

  ngOnInit() {
    this.user = new User();

    this.publicId = this.route.snapshot.params['publicId'];
    
    this.userService.getUser(this.publicId ?? '')
      .subscribe(data => {
        console.log(data)
        this.user = data;
      }, error => console.log(error));
  }

  updateUser() {
    this.userService.updateUser(this.publicId ?? '', this.user)
      .subscribe(data => {
        console.log(data);
        this.user = new User();
        this.gotoList();
      }, error => console.log(error));
  }

  onSubmit() {
    this.updateUser();    
  }

  gotoList() {
    this.router.navigate(['/users']);
  }

}

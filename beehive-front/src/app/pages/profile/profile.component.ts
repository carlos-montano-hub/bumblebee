import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../modules/shared.module';
import { AuthService } from '../../services/auth/auth.service';
import { User } from '../../services/user/user.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  user?: User;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.fetchUserProfile();
  }

  async fetchUserProfile() {
    try {
      this.user = await this.authService.getProfile();
    } catch (error) {
      console.error('Error fetching user profile:', error);
    }
  }
}

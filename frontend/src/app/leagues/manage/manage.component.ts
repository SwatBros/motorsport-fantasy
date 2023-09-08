import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizedPageComponent } from 'src/app/shared/authorized-page/authorized-page.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-manage',
  standalone: true,
  imports: [CommonModule, AuthorizedPageComponent, RouterOutlet],
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.sass']
})
export class ManageComponent {
}

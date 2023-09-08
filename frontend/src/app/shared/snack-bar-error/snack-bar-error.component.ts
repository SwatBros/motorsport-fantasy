import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_SNACK_BAR_DATA, MatSnackBarModule, MatSnackBarRef } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-snack-bar-error',
  standalone: true,
  imports: [CommonModule, MatSnackBarModule, MatButtonModule],
  templateUrl: './snack-bar-error.component.html',
  styleUrls: ['./snack-bar-error.component.sass']
})
export class SnackBarErrorComponent {
  constructor(public snackBarRef: MatSnackBarRef<any>, @Inject(MAT_SNACK_BAR_DATA) public data: string) {}
}

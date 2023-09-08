import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-league-name',
  standalone: true,
  imports: [CommonModule, MatInputModule,
    MatButtonModule, MatCardModule, FormsModule, ReactiveFormsModule],
  templateUrl: './league-name.component.html',
  styleUrls: ['./league-name.component.sass']
})
export class LeagueNameComponent {
  @Input() title!: string
  @Input() action!: string
  @Output() pressed = new EventEmitter<string>();

  name = new FormControl('', Validators.required);

  onSubmit() {
    if (this.name.invalid) {
      return;
    }

    this.pressed.emit(this.name.value!);
  }
}

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SnackBarErrorComponent } from './snack-bar-error.component';

describe('SnackBarErrorComponent', () => {
  let component: SnackBarErrorComponent;
  let fixture: ComponentFixture<SnackBarErrorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SnackBarErrorComponent]
    });
    fixture = TestBed.createComponent(SnackBarErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

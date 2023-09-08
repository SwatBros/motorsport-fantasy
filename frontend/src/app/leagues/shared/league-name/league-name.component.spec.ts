import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeagueNameComponent } from './league-name.component';

describe('LeagueNameComponent', () => {
  let component: LeagueNameComponent;
  let fixture: ComponentFixture<LeagueNameComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LeagueNameComponent]
    });
    fixture = TestBed.createComponent(LeagueNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

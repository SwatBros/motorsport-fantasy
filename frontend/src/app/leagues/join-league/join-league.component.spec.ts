import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinLeagueComponent } from './join-league.component';

describe('JoinLeagueComponent', () => {
  let component: JoinLeagueComponent;
  let fixture: ComponentFixture<JoinLeagueComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [JoinLeagueComponent]
    });
    fixture = TestBed.createComponent(JoinLeagueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

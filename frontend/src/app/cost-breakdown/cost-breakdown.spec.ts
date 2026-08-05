import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CostBreakdownCard } from './cost-breakdown-card';

describe('CostBreakdown', () => {
  let component: CostBreakdownCard;
  let fixture: ComponentFixture<CostBreakdownCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CostBreakdownCard],
    }).compileComponents();

    fixture = TestBed.createComponent(CostBreakdownCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

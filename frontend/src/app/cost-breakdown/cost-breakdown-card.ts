import { Component, Input } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { CostBreakdownObject } from '../services/charging.service';

@Component({
  selector: 'app-cost-breakdown',
  imports: [CurrencyPipe, DecimalPipe],
  templateUrl: './cost-breakdown.html',
  styleUrl: './cost-breakdown.css',
})
export class CostBreakdownCard {
  @Input({ required: true }) result!: CostBreakdownObject;
}

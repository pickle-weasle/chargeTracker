import { Component, OnInit, signal } from '@angular/core';
import { ChargingService, Session, Station } from './services/charging.service';
import { CostBreakdownCard } from './cost-breakdown/cost-breakdown-card';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CostBreakdownCard],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  stations = signal<Station[]>([]);
  activeSession = signal<Session | null>(null);
  costBreakdown = signal<any>(null);

  constructor(private chargingService: ChargingService) {}

  ngOnInit() {
    this.getStationsAndSession();
  }

  getStationsAndSession() {
    this.chargingService.getStations().subscribe((stations) => this.stations.set(stations));
    this.chargingService
      .getActiveSessions()
      .subscribe((sessions) => this.activeSession.set(sessions[0] ?? null));
  }

  startSession(stationId: string) {
    console.log('starting session');
    this.chargingService.startSession(stationId).subscribe(() => this.getStationsAndSession());
  }

  stopSession(sessionId: string) {
    this.chargingService.stopSession(sessionId).subscribe((costResult) => {
      console.log('stopping session');
      this.costBreakdown.set(costResult);
      this.getStationsAndSession();
    });
  }
}

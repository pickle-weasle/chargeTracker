import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Station {
  id: string;
  name: string;
  address: string;
  chargingSpeed: number;
}
export interface Session {
  id: string;
  stationId: string;
  startTime: string;
  endTime?: string;
  cost?: number;
}

export interface CostBreakdownObject {
  id: string;
  endTime: string;
  totalCost: number;
  peakUnits: number;
  offPeakUnits: number;
  peakCost: number;
  offPeakCost: number;
}

@Injectable({ providedIn: 'root' })
export class ChargingService {
  private backendUrl = 'http://localhost:9000';

  constructor(private http: HttpClient) {}

  getStations() {
    return this.http.get<Station[]>(`${this.backendUrl}/stations`);
  }
  getActiveSessions() {
    return this.http.get<Session[]>(`${this.backendUrl}/sessions/active`);
  }
  startSession(stationId: string) {
    console.log('service start session')
    return this.http.post<Session>(`${this.backendUrl}/sessions`, { stationId });
  }
  stopSession(id: string) {
    return this.http.post<CostBreakdownObject>(`${this.backendUrl}/sessions/${id}/stop`, {});
  }
}

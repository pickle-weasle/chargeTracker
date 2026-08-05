package models

import java.time.Instant

case class Session(
    id: String,
    stationId: String,
    startTime: Instant,
    peakRate: BigDecimal,
    offPeakRate: BigDecimal,
    chargingSpeed: Double,
    endTime: Option[Instant], // created first without, updated later
    cost: Option[BigDecimal] // calculated when tracking session ends
)

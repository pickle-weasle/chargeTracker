# Notes
### runing list of things to address
CORS policy
close routing direct to play app on port 9000

# Sessions & stations


Session
- id: String
- stationId: String
- startTime: instant
- peakRate: BigDecimal // in case it changes in the future
- offPeakRate: BigDecimal // in case it changes in the future
- chargingSpeed: Double
- endTime: Option[Instant]
- cost: Option[BigDecimal]

Station
- id: String
- name: String
- address: String
- chargingSpeed: Double
package models

import anorm._
import play.api.libs.json.{Format, Json}

import java.time.Instant
import anorm.SqlParser._

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

object Session {
  implicit val format: Format[Session] = Json.format[Session]

  val parser: RowParser[Session] = {
    get[String]("id") ~
    get[String]("station_id") ~
    get[Instant]("start_time") ~
    get[BigDecimal]("peak_rate") ~
    get[BigDecimal]("off_peak_rate") ~
    get[Double]("charging_speed") ~
    get[Option[Instant]]("end_time") ~
    get[Option[BigDecimal]]("cost")  map {
      case id ~ stationId ~ startTime ~ peakRate ~ offPeakRate ~ chargingSpeed ~ endTime ~ cost =>
        Session(id, stationId, startTime, peakRate, offPeakRate, chargingSpeed, endTime, cost)
    }
  }
}
package controllers

import models.Session
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import pricing.Pricing

import java.time.Instant
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.collection.concurrent.TrieMap

@Singleton
class SessionController @Inject()(cc:ControllerComponents) extends AbstractController(cc){
  def start: Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[CreateSessionRequest].fold(
      _ => BadRequest(Json.obj("error" -> "Invalid request body")),
      req => {
        val alreadyActive = SessionController.sessions.values.exists(_.endTime.isEmpty)
        if (alreadyActive) Conflict(Json.obj("error" -> "A session is already in progress"))
        else StationController.stations.find(_.id == req.stationId) match {
          case None => NotFound(Json.obj("error" -> "Station not found"))
          case Some(station) =>
            val id = UUID.randomUUID().toString
            val session = Session(id, station.id, Instant.now(), Pricing.peakPricePerUnit, Pricing.offPeakPricePerUnit, station.chargingSpeed)
            SessionController.sessions.put(id, session)
            Ok(Json.toJson(session))
        }
      }
    )
  }

  def stop(id: String): Action[AnyContent] = Action {
    SessionController.sessions.get(id) match {
      case None => NotFound(Json.obj("error" -> "Session not found"))
      case Some(s) if s.endTime.isDefined => Conflict(Json.obj("error" -> "Session already stopped"))
      case Some(s) =>
        val now = Instant.now()
        val breakdown = Pricing.calculateCost(s.startTime, now, s.chargingSpeed)
        SessionController.sessions.put(id, s.copy(endTime = Some(now), cost = Some(breakdown.totalCost)))
        Ok(Json.obj(
          "id" -> s.id, "endTime" -> now.toString, "totalCost" -> breakdown.totalCost,
          "peakUnits" -> breakdown.totalPeakUnits, "offPeakUnits" -> breakdown.totalOffPeakUnits,
          "peakCost" -> breakdown.peakCost, "offPeakCost" -> breakdown.offPeakCost
        ))
    }
  }

  def active: Action[AnyContent] = Action {
    Ok(Json.toJson(SessionController.sessions.values.filter(_.endTime.isEmpty).toList))
  }

}

object SessionController {
  val sessions: TrieMap[String, Session] = TrieMap.empty
}

case class CreateSessionRequest(stationId: String)
object CreateSessionRequest {
  implicit val format: Format[CreateSessionRequest] = Json.format[CreateSessionRequest]
}

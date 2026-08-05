package controllers

import models.Station
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class StationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def list: Action[AnyContent] = Action {Ok(Json.toJson(StationController.stations))}

}

object StationController {
  val stations: List[Station] = List(
    Station("1", "Dublin Airport", "Terminal 2, Dublin", 150.0),
    Station("2", "IFSC", "International Financial Services Centre, Dublin 1", 22.0),
    Station("3", "Dundrum Town Centre", "Sandyford Road, Dundrum, Dublin 16", 50.0),
    Station("4", "Liffey Valley", "Fonthill Road, Dublin 22", 50.0),
    Station("5", "The Square Tallaght", "Tallaght, Dublin 24", 22.0),
    Station("6", "Blanchardstown Centre", "Blanchardstown, Dublin 15", 50.0),
    Station("7", "Grand Canal Dock", "Grand Canal Dock, Dublin 2", 7.4),
    Station("8", "Phoenix Park Visitor Centre", "Phoenix Park, Dublin 8", 22.0),
    Station("9", "Sandyford Business District", "Sandyford, Dublin 18", 150.0),
    Station("10", "Croke Park", "Jones's Road, Dublin 3", 10000.0)
  )
}

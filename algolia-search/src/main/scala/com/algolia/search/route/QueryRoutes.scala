package com.algolia.search.route

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.algolia.search.actor.QueryActor
import com.algolia.search.actor.QueryActor.{GetCount, GetPopularity}
import com.algolia.search.model.Entities.{CountDTO, PopularityDTO}
import akka.actor.typed.scaladsl.AskPattern._

import scala.concurrent.Future

class QueryRoutes(queryActor: ActorRef[QueryActor.Command])(implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import com.algolia.search.serializer.JsonFormats._

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getPopularity(date: String, size: Int): Future[PopularityDTO] =
    queryActor.ask(GetPopularity(date, size,  _))
  def getCount(date: String): Future[CountDTO] =
    queryActor.ask(GetCount(date, _))

  val queryRoutes: Route = pathPrefix("queries") {
    path("count" / Segment) { date =>
        get {
          complete(getCount(date))
        }
    } ~ path("popular" / Segment) { date =>
      get {
        parameter("size") { size =>
          complete(getPopularity(date, size.toInt))
        }
      }
    }
  }

}

package com.algolia.search.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.algolia.search.model.Entities
import com.algolia.search.model.Entities._
import com.algolia.search.reader._

object QueryActor {
  // actor protocol
  sealed trait Command
  final case class GetCount(date: String, replyTo: ActorRef[CountDTO]) extends Command
  final case class GetPopularity(date: String, size: Int, replyTo: ActorRef[PopularityDTO]) extends Command
  def apply(): Behavior[Command] = registry()

  private def registry(): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetCount(date, replyTo) =>
        replyTo ! CountComposer.getResponse(date)
        Behaviors.same
      case GetPopularity(date, size, replyTo) =>
        replyTo ! PopularityComposer.getResponse(date, size)
        Behaviors.same
    }


}

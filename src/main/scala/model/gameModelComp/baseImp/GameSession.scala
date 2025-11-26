package model.gameModelComp.baseImp

import java.util.UUID

case class GameSession(
  sessionId: String,
  var player1: Option[Player] = None,
  var player2: Option[Player] = None,
  var player1Id: Option[String] = None,
  var player2Id: Option[String] = None,
  var currentPlayerId: Option[String] = None,
  var isStarted: Boolean = false
) {
  def isReady: Boolean = player1.isDefined && player2.isDefined
}

object GameSession {
  def generateSessionId(): String = {
    UUID.randomUUID().toString.take(8).toUpperCase
  }
}

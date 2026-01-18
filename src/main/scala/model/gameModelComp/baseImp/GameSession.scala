package model.gameModelComp.baseImp

import java.util.UUID
import util.command.GameState

case class GameSession(
  sessionId: String,
  var player1: Option[Player] = None,
  var player2: Option[Player] = None,
  var player1Id: Option[String] = None,
  var player2Id: Option[String] = None,
  var currentPlayerId: Option[String] = None,
  var isStarted: Boolean = false,
  var grid: Option[Grid] = None,
  var deck: Option[Deck] = None,
  var currentPlayer: Option[Player] = None,
  var gameState: Option[GameState] = None,
  var gridPlacements: scala.collection.mutable.Map[(Int, Int), String] = scala.collection.mutable.Map()
) {
  def isReady: Boolean = player1.isDefined && player2.isDefined
}

object GameSession {
  def generateSessionId(): String = {
    UUID.randomUUID().toString.take(8).toUpperCase
  }
}

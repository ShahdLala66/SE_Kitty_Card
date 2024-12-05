// src/main/scala/util/GameEvents.scala
package util

sealed trait GameEvent

case class PrintMeh(color: String) extends GameEvent
case class PrintBadChoice(color: String) extends GameEvent
case class PlayerTurn(playerName: String) extends GameEvent
case class CardDrawn(playerName: String, card: String) extends GameEvent
case class InvalidPlacement() extends GameEvent
case class ShowColoredCat(color: String) extends GameEvent
case class CardPlacementSuccess(x: Int, y: Int, card: String, points: Int) extends GameEvent
case class GameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int) extends GameEvent
case class UndoEvent(state: GameState) extends GameEvent
case class RedoEvent(state: GameState) extends GameEvent
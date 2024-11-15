// src/main/scala/util/GameEvents.scala
package util

sealed trait GameEvent
case class PlayerTurn(playerName: String) extends GameEvent
case class CardDrawn(playerName: String, card: String) extends GameEvent
case class InvalidPlacement() extends GameEvent
case class CardPlacementSuccess(x: Int, y: Int, card: String, points: Int) extends GameEvent
case class GameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int) extends GameEvent
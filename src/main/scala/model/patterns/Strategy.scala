// src/main/scala/model/Strategy.scala
package model.patterns

import model.Game

trait Strategy {
  def playTurn(game: Game): Unit
}
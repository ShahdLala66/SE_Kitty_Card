// src/main/scala/model/Strategy.scala
package model.patterns

import model.logik.Game

trait Strategy {
  def playTurn(game: Game): Unit
}
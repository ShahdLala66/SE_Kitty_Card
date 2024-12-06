// src/main/scala/model/Strategy.scala
package model.patterns

import model.Logik.Game

trait Strategy {
  def playTurn(game: Game): Unit
}
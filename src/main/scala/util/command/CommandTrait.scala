// src/main/scala/util/Command.scala
package util.command

import util.GameState

trait CommandTrait {
  var previousState: GameState = _
  def execute(): Unit
  def undo(): Unit
  def redo(): Unit
}
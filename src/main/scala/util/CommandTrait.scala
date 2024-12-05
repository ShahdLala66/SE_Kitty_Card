// src/main/scala/util/Command.scala
package util

trait Command {
  var previousState: GameState = _
  def execute(): Unit
  def undo(): Unit
  def redo(): Unit
}
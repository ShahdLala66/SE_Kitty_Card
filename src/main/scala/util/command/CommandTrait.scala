// src/main/scala/util/Command.scala
package util.command

trait CommandTrait {
  var previousState: GameState = _
  def execute(): Unit
  def undo(): Unit
  def redo(): Unit
}
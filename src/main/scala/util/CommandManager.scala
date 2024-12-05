package util

import scala.collection.mutable.Stack

class CommandManager {
  private val undoStack: Stack[GameState] = Stack[GameState]()
  private val redoStack: Stack[GameState] = Stack[GameState]()

  def executeCommand(command: Command, currentState: GameState): GameState = {
    undoStack.push(currentState) // Save the current state before executing the command
    command.execute() // Execute the command
    currentState // placeholder return
  }

  def undo(): Option[GameState] = {
    if (undoStack.nonEmpty) {
      val previousState = undoStack.pop() // Get the previous state
      redoStack.push(previousState) // Save the previous state before undoing
      Some(previousState) // placeholder return, wrap the previous state in an Option
    }
    else {
      None // placeholder return
    }
  }

  def redo(): Option[GameState] = {
    if (redoStack.nonEmpty) {
      val nextState = redoStack.pop() //
      undoStack.push(nextState)
      Some(nextState)
    }
    else {
      None 
    }
  }
}
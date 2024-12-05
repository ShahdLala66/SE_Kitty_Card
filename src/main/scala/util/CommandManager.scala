// src/main/scala/util/CommandManager.scala
package util

import scala.collection.mutable.Stack

class CommandManager {
    private val undoStack: Stack[Command] = Stack()
    private val redoStack: Stack[Command] = Stack()

    def executeCommand(command: Command, currentState: GameState): GameState = {
        command.execute()
        undoStack.push(command)
        redoStack.clear()
        currentState
    }

    def undo(): Option[GameState] = {
        if (undoStack.nonEmpty) {
            val command = undoStack.pop()
            command.undo()
            redoStack.push(command)
            Some(command.previousState)
        } else {
            None
        }
    }

    def redo(): Option[GameState] = {
        if (redoStack.nonEmpty) {
            val command = redoStack.pop()
            command.redo()
            undoStack.push(command)
            Some(command.previousState)
        } else {
            None
        }
    }
}
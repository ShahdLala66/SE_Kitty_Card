
package util.command

import scala.collection.mutable
import scala.collection.mutable.Stack
import scala.compiletime.uninitialized

class CommandManager {
    private val undoStack: mutable.Stack[CommandTrait] = mutable.Stack()
    private val redoStack: mutable.Stack[CommandTrait] = mutable.Stack()

    def executeCommand(command: CommandTrait, currentState: GameState): GameState = {
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

trait CommandTrait {
    var previousState: GameState = uninitialized
    def execute(): Unit
    def undo(): Unit
    def redo(): Unit
}
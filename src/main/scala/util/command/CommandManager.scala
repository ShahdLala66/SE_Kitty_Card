// src/main/scala/util/CommandManager.scala
package util.command

import scala.collection.mutable
import scala.collection.mutable.Stack

class CommandManager {
    private val undoStack: mutable.Stack[CommandTrait] = mutable.Stack()
    private val redoStack: mutable.Stack[CommandTrait] = mutable.Stack()

    def executeCommand(command: CommandTrait, currentState: GameState): GameState = {
       // führt Befehl aus, speichert current im undoStack und löscht den redoStack
        command.execute()
        undoStack.push(command)
        redoStack.clear()
        currentState
    }

    def undo(): Option[GameState] = {
        //wenn undoStack nicht leer ist, führe undo aus, speichere im redoStack und gib den vorherigen Zustand zurück
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
        //wenn redoStack nicht leer ist, führe redo aus, speichere im undoStack und gib den vorherigen Zustand zurück
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
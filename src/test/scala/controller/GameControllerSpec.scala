// src/test/scala/controller/GameControllerSpec.scala
package controller

import aview.Tui
import util.*
import util.command.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameControllerSpec extends AnyWordSpec with Matchers {

    "A GameController" should {

        "execute a command" in {
            val controller = new GameController()
            val observer = new Tui()
            controller.setObserver(observer)

            val command = new MockCommand()
            controller.executeCommand(command)

            // Verify that the command was executed
            command.executed shouldBe true
        }

        "undo a command" in {
            val controller = new GameController()
            val observer = new Tui()
            controller.setObserver(observer)

            val command = new MockCommand()
            controller.executeCommand(command)
            controller.undo()

            // Verify that the command was undone
            command.undone shouldBe true
        }

        "redo a command" in {
            val controller = new GameController()
            val observer = new Tui()
            controller.setObserver(observer)

            val command = new MockCommand()
            controller.executeCommand(command)
            controller.undo()
            controller.redo()

            // Verify that the command was redone
            command.redone shouldBe true
        }
    }
}

// MockCommand class for testing purposes
class MockCommand extends CommandTrait {
    var executed = false
    var undone = false
    var redone = false

    override def execute(): Unit = {
        executed = true
    }

    override def undo(): Unit = {
        undone = true
    }

    override def redo(): Unit = {
        redone = true
    }
}
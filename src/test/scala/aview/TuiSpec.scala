// src/test/scala/aview/TuiSpec.scala
package aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import util.*
import util.Observer.{CardDrawn, CardPlacementSuccess, GameOver, InvalidPlacement, PlayerTurn, RedoEvent, TotalPoints, UndoEvent}
import util.command.GameState

class TuiSpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A Tui" should {

        "set undo callback" in {
            val tui = new Tui()
            var undoCalled = false
            tui.setUndoCallback(() => undoCalled = true)
            tui.undoCallback()
            undoCalled should be(true)
        }

        "set redo callback" in {
            val tui = new Tui()
            var redoCalled = false
            tui.setRedoCallback(() => redoCalled = true)
            tui.redoCallback()
            redoCalled should be(true)
        }

        "run and start single player mode" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("1\n1\nPlayer1\n".getBytes)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withIn(inputStream) {
                Console.withOut(outputStream) {
                    tui.run()
                }
            }
            val output = outputStream.toString
            output should include("Welcome to the Kitty Card Game!")
            output should include("Starting Feed the kitties mode for Player1...")
        }

        "run and start multiplayer mode" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("2\nPlayer1\nPlayer2\n".getBytes)
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withIn(inputStream) {
                Console.withOut(outputStream) {
                    tui.run()
                }
            }
            val output = outputStream.toString
            output should include("Welcome to the Kitty Card Game!")
            output should include("Starting multiplayer game between Player1 and Player2...")
        }

        "print the welcome message" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.welcomeMessage()
            }
            outputStream.toString should include ("Welcome to the Kitty Card Game!")
        }

        "select single player option 'Feed the kitties' when input is 1" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("1\n".getBytes)
            Console.withIn(inputStream) {
                tui.selectSinglePlayerOption() should be("Feed the kitties")
            }
        }

        "select single player option 'Play with the Kitty Card Boss' when input is 2" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("2\n".getBytes)
            Console.withIn(inputStream) {
                tui.selectSinglePlayerOption() should be("Play with the Kitty Card Boss")
            }
        }

        "print colored cats in a loop" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printCatLoop()
            }
            val output = outputStream.toString
            tui.colors.values.foreach { color =>
                output should include(s"$color ∧,,,∧")
            }
        }



        "default to 'Feed the kitties' when input is invalid" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("invalid\n".getBytes)
            Console.withIn(inputStream) {
                tui.selectSinglePlayerOption() should be("Feed the kitties")
            }
        }

        "select single player mode when input is 1" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("1\n".getBytes)
            Console.withIn(inputStream) {
                tui.selectGameMode() should be ("Single")
            }
        }

        "select multiplayer mode when input is 2" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("2\n".getBytes)
            Console.withIn(inputStream) {
                tui.selectGameMode() should be ("Multiplayer")
            }
        }

        "default to single player mode when input is invalid" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("invalid\n".getBytes)
            Console.withIn(inputStream) {
                tui.selectGameMode() should be ("Single")
            }
        }

        "prompt for player name" in {
            val tui = new Tui()
            val inputStream = new java.io.ByteArrayInputStream("Player1\n".getBytes)
            Console.withIn(inputStream) {
                tui.promptPlayerName("Enter your name: ") should be ("Player1")
            }
        }

        "print colored cat" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printColoredCat("\u001b[32m")
            }
            outputStream.toString should include ("\u001b[32m ∧,,,∧")
        }

        "print colored message" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printColoredMessage("Green", "test message")
            }
            outputStream.toString should include ("\u001b[32m ∧,,,∧")
            outputStream.toString should include ("test message")
        }

        "print bad choice message" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printBadChoice("Red")
            }
            outputStream.toString should include ("bad choice")
        }

        "print meh message" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.printMeh("Blue")
            }
            outputStream.toString should include ("meh")
        }

        "handle player turn update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(PlayerTurn("Player1"))
            }
            outputStream.toString should include ("Player1's turn.")
        }

        "handle card drawn update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(CardDrawn("Player1", "Card1"))
            }
            outputStream.toString should include ("Player1 drew: Card1")
        }

        "handle invalid placement update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(InvalidPlacement())
            }
            outputStream.toString should include ("Invalid placement.")
        }

        "handle card placement success update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(CardPlacementSuccess(1, 2, "Card1", 10))
            }
            outputStream.toString should include ("Card placed at (1, 2): Card1.")
        }

        "handle game over update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(GameOver("Player1", 20, "Player2", 15))
            }
            outputStream.toString should include ("Game over!")
            outputStream.toString should include ("Player1 wins!")
        }

        "handle total points update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(TotalPoints(20, 15))
            }
            outputStream.toString should include ("Total points: Player 1: 20, Player 2: 15")
        }

        "handle undo event update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(UndoEvent(new GameState(null, List(), 0, 0)))
            }
            outputStream.toString should include ("Undo performed.")
        }

        "handle redo event update" in {
            val tui = new Tui()
            val outputStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outputStream) {
                tui.update(RedoEvent(new GameState(null, List(), 0, 0)))
            }
            outputStream.toString should include ("Redo performed.")
        }
    }
}
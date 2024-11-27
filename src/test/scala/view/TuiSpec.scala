package view

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayOutputStream, PrintStream}
import util._

class TuiSpec extends AnyWordSpec with Matchers {

    "The Tui" should {

        "print the correct welcome message" in {
            val tui = new Tui
            // Umleiten der Ausgabe
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.welcomeMessage()
            }

            val output = outStream.toString
            output should include ("Welcome to the Kitty Card Game!")
            output should include ("Players take turns drawing and placing cards on the grid.")
            output should include ("Earn points by placing cards on matching colors or white squares.")
        }

        "print correct player turn message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(PlayerTurn("Alice"))
            }

            val output = outStream.toString
            output should include ("Alice's turn.")
        }

        "print correct card drawn message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(CardDrawn("Bob", "Red Card"))
            }

            val output = outStream.toString
            output should include ("Bob drew: Red Card")
        }

        "print correct invalid placement message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(InvalidPlacement())
            }

            val output = outStream.toString
            output should include ("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
        }

        "print correct card placement success message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(CardPlacementSuccess(1, 2, "Green Card", 10))
            }

            val output = outStream.toString
            output should include ("Card placed at (1, 2): Green Card. Points earned: 10.")
        }

        "print correct game over message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(GameOver("Alice", 50, "Bob", 40))
            }

            val output = outStream.toString
            output should include ("Game over!")
            output should include ("Alice's final score: 50")
            output should include ("Bob's final score: 40")
            output should include ("Alice wins!")
        }

        "print correct game over message part 2" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(GameOver("Alice", 40, "Bob", 69))
            }

            val output = outStream.toString
            output should include("Game over!")
            output should include("Alice's final score: 40")
            output should include("Bob's final score: 69")
            output should include("Bob wins!")
        }

        "print correct game over tie message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(GameOver("Alice", 50, "Bob", 50))
            }

            val output = outStream.toString
            output should include("Game over!")
            output should include("Alice's final score: 50")
            output should include("Bob's final score: 50")
            output should include("It's a tie!")
        }

        "print correct bad choice message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.printBadChoice("Red")
            }

            val output = outStream.toString
            output should include ("bad choice")
        }

        "print correct meh message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.printMeh("White")
            }

            val output = outStream.toString
            output should include ("meh")
        }

        "print the cat in all colors" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.printCatLoop()
            }

            val output = outStream.toString
            tui.colors.values.foreach { color =>
                output should include(s"$color ∧,,,∧")
            }
        }


    }
}


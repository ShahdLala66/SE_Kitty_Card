package aview

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

        "print correct meh not implemented message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(PrintMeh("White"))
            }

            val output = outStream.toString
            output should include("meh not implemented yet")
        }

        "print correct bad choice not implemented message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(PrintBadChoice("Red"))
            }

            val output = outStream.toString
            output should include("bad not implemented yet")
        }

        "print correct show cat not implemented message" in {
            val tui = new Tui
            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                tui.update(ShowColoredCat("Green"))
            }

            val output = outStream.toString
            output should include("show cat not implemented yet")
        }

        "run method" should {
            "start single player mode with 'Feed the kitties' option" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("1\n1\nAlice\n".getBytes)
                Console.withIn(inStream) {
                    val outStream = new ByteArrayOutputStream()
                    Console.withOut(new PrintStream(outStream)) {
                        tui.run()
                    }
                    val output = outStream.toString
                    output should include("Starting Feed the kitties mode for Alice...")
                }
            }

            "start multiplayer mode" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("2\nAlice\nBob\n".getBytes)
                Console.withIn(inStream) {
                    val outStream = new ByteArrayOutputStream()
                    Console.withOut(new PrintStream(outStream)) {
                        tui.run()
                    }
                    val output = outStream.toString
                    output should include("Starting multiplayer game between Alice and Bob...")
                }
            }
        }

        "selectGameMode method" should {
            "return 'Single' for input 1" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("1\n".getBytes)
                Console.withIn(inStream) {
                    val result = tui.selectGameMode()
                    result should be("Single")
                }
            }

            "return 'Multiplayer' for input 2" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("2\n".getBytes)
                Console.withIn(inStream) {
                    val result = tui.selectGameMode()
                    result should be("Multiplayer")
                }
            }

            "default to 'Single' for invalid input" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("invalid\n".getBytes)
                Console.withIn(inStream) {
                    val result = tui.selectGameMode()
                    result should be("Single")
                }
            }
        }

        "selectSinglePlayerOption method" should {
            "return 'Feed the kitties' for input 1" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("1\n".getBytes)
                Console.withIn(inStream) {
                    val result = tui.selectSinglePlayerOption()
                    result should be("Feed the kitties")
                }
            }

            "return 'Play with the Kitty Card Boss' for input 2" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("2\n".getBytes)
                Console.withIn(inStream) {
                    val result = tui.selectSinglePlayerOption()
                    result should be("Play with the Kitty Card Boss")
                }
            }

            "default to 'Feed the kitties' for invalid input" in {
                val tui = new Tui
                val inStream = new java.io.ByteArrayInputStream("invalid\n".getBytes)
                Console.withIn(inStream) {
                    val result = tui.selectSinglePlayerOption()
                    result should be("Feed the kitties")
                }
            }
        }
    }
}

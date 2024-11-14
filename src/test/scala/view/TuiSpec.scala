package view

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TuiSpec extends AnyWordSpec with Matchers {

    "A CatPrint" should {

        "print the cat in all colors in a loop" in {
            val catPrint = new Tui
            noException should be thrownBy catPrint.printCatLoop()
        }

        "print the cat in a specific color" in {
            val catPrint = new Tui
            noException should be thrownBy catPrint.printCatInColor("Green")
        }

        "print a bad choice message" in {
            val catPrint = new Tui
            noException should be thrownBy catPrint.printBadChoice("Red")
        }

        "print a meh message" in {
            val catPrint = new Tui
            noException should be thrownBy catPrint.printMeh("Blue")
        }

        "reset color after printing" in {
            val catPrint = new Tui
            val outStream = new java.io.ByteArrayOutputStream()
            Console.withOut(outStream) {
                catPrint.printCatInColor("Green")
            }
            outStream.toString should include("\u001b[0m")
        }
    }
}
import model.Grid

object KittyCardGame {

    def main(args: Array[String]): Unit = {

        val eol = sys.props("line.separator") // End of line character

        def printHeading(): Unit =
            println(Console.MAGENTA + "Welcome to the Kitty Card Game!" + Console.RESET + eol)

        def card(color: String, value: Int): String = {
            val brightWhite = "\u001b[97m"
            val cardTop = color + " ∧,,,∧"
            val cardMiddle = s"( ̳• · •̳) " + brightWhite + value
            val cardBottom = color + "/    づ♡" + Console.RESET
            // s string interpolator, multiline string
            val cardContent = s"$cardTop$eol$cardMiddle$eol$cardBottom$eol"
            cardContent
        }

        // Cards in two hands
        val hand1 = List("S", "6") // First hand (left)
        val hand2 = List("I", "9") // Second hand (right)

        // Function to print a hand of cards
        def printHand(hand: List[String]): Unit = {
            val topBorder = hand.map(_ => "  +---+").mkString + eol
            val cardValues = hand.map(card => s"  | $card |").mkString + eol
            val bottomBorder = hand.map(_ => "  +---+").mkString + eol
            print(topBorder + cardValues + bottomBorder + eol)
        }

        printHeading()

        printHand(hand1)

        val grid = Grid()
        println(grid.display())

        printHand(hand2)

        println(card(Console.BLUE, 3))
        println(card(Console.RED, 10))
        println(card(Console.GREEN, 8))
        print(card(Console.YELLOW, 1))


    }
}
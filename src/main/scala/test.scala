object test {

    def main(args: Array[String]): Unit = {

        val eol = sys.props("line.separator") // End of line character

        def bar(cellWidth: Int = 15, cellNum: Int = 3): String =
            ("+" + "-" * cellWidth) * cellNum + "+" + eol

        def cells(cellWidth: Int = 15, cellNum: Int = 3): String =
            (("|" + " " * cellWidth) * cellNum + "|" + eol) * 4

        def mesh(cellWidth: Int = 15, cellNum: Int = 3): String =
            (bar(cellWidth, cellNum) + cells(cellWidth, cellNum)) * cellNum + bar(cellWidth, cellNum)

        def printHeading(): Unit =
            println(Console.MAGENTA + "Welcome to the Kitty Card Game!" + Console.RESET+eol)
        def card(color: String, value: Int): String = {
            val brightWhite = "\u001b[97m"
            val cardTop = (color + " ∧,,,∧")
            val cardMiddle = s"( ̳• · •̳) " + brightWhite + value
            val cardBottom = (color + "/    づ♡" + Console.RESET)
            // s string interpolator, multiline string
            val cardContent = s"$cardTop$eol$cardMiddle$eol$cardBottom$eol"
            cardContent
        }

            // Cards in two hands
            val hand1 = List("S", "6") // First hand (left)
            val hand2 = List("I", "9") // Second hand (right)

            // Call the methods to print the hands and the cats
            printHand(hand1)

            //
            //
            // Function to print a hand of cards
            def printHand(hand: List[String]): Unit = {
                // Print the top border of the cards
                for (_ <- hand) {
                    print("  +---+")
                }
                println()

                // Print the card values
                for (card <- hand) {
                    print(s"  | $card |")
                }
                println()

                // Print the bottom border of the cards
                for (_ <- hand) {
                    print("  +---+")
                }
                println()
                println() // Blank line after the hand
            }

        printHeading()
        println(mesh())
        println(card(Console.BLUE, 3))
        println(card(Console.RED, 10))
        println(card(Console.GREEN, 8))
        print(card(Console.YELLOW, 1))

        printHand(hand2)

    }
}
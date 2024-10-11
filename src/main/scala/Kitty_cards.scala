object Kitty_cards {
    def main(args: Array[String]): Unit = {
        println("Welcome to the Kitty Card Game!")
        // Create an instance of the Kitty_cards class to run its logic
        new Kitty_cards()
    }

    // Define the Kitty_cards class outside of the main method
    private class Kitty_cards {
        // Cards in two hands
        private val hand1 = List("S", "6") // First hand (left)
        private val hand2 = List("I", "9") // Second hand (right)

        // Call the methods to print the hands and the cats
        printHand(hand1)
        printCats()
        printHand(hand2)

        //
        //
        // Function to print a hand of cards
        private def printHand(hand: List[String]): Unit = {
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

        //setting up corina branch
        // Function to print the cats
        private def printCats(): Unit = {
            println("________________________________________")
            println("|            |            |            |")
            println("|            |   ≽^•⩊•^≼  |            |")
            println("|            |            |            |")
            println("__________________________________________")
            println("|            |            |            |")
            println("|            |  ≽^•⩊•^≼   |  ≽^•⩊•^≼   |")
            println("|            |            |            |")
            println("__________________________________________")
            println("|            |            |             |")
            println("|  ≽^•⩊•^≼   |  ≽^•⩊•^≼  |             |")
            println("|            |            |             |")
            println("________________________________________")
            //kittys
            //setting up shahed lol
            println()
            //setting up shahed branch
        }
    }
}
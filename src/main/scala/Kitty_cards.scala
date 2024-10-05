object KittyCardGame {
  def main(args: Array[String]): Unit = {
    println("Welcome to the Kitty Card Game!")
    // Create an instance of the Kitty_cards class to run its logic
    new Kitty_cards()
  }

  // Define the Kitty_cards class outside of the main method
  class Kitty_cards {
    // Cards in two hands
    val hand1 = List("S", "6") // First hand (left)
    val hand2 = List("I", "9") // Second hand (right)

    // Call the methods to print the hands and the cats
    printHand(hand1)
    printCats()
    printHand(hand2)
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

    // Function to print the cats
    def printCats(): Unit = {
       println("__________________________________________")
      println("|            |  ≽^•⩊•^≼   |            |")
      println("__________________________________________")
      println("| ≽^•⩊•^≼    | ≽^•⩊•^≼   | ≽^•⩊•^≼    |" )
      println("__________________________________________")
      println("| ≽^•⩊•^≼    | ≽^•⩊•^≼   |             |" )
      println("__________________________________________")
//setting up shahed lol
      println()
    }
  }
}
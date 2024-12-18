package aview

import controller.GameController
import util.*

class Tui(gameController: GameController) extends Observer {

  private val inputProvider: InputProvider = new ConsoleProvider

  private[aview] val colors = Map(
    "Green" -> "\u001b[32m",
    "Brown" -> "\u001b[33m",
    "Purple" -> "\u001b[35m",
    "Blue" -> "\u001b[34m",
    "Red" -> "\u001b[31m",
    "White" -> "\u001b[37m"
  )

  private def printColoredCat(color: String): Unit = {
    println(s"$color ∧,,,∧")
    println(s"$color( ̳• · •̳)")
    println(s"$color/    づ♡")
    println("\u001b[0m") // Reset color after printing
  }

  // Method to print the cat in all colors in a loop
  def printCatLoop(): Unit = {
    for (color <- colors.values) {
      printColoredCat(color)
      Thread.sleep(500) // Optional: Delay for visual effect
    }
  }

  def promptForPlayerName(): Unit = {
    println(s"Enter the name for Player 1:")
    val player1 = inputProvider.getInput
    println(s"Enter the name for Player 2:")
    val player2 = inputProvider.getInput
    gameController.promptForPlayerName(player1, player2)
  }

  def start(): Unit = {
    welcomeMessage()
    promptForPlayerName()
  }

  def printBadChoice(color: String): Unit = {
    val colorCode = colors.getOrElse(color, "\u001b[0m")
    println(s"$colorCode ∧,,,∧")
    println(s"$colorCode ( ̳- · -̳)")
    println(s"$colorCode/ づbad choiceづ")
    println("\u001b[0m")
  }

  def printMeh(color: String): Unit = {
    val colorCode = colors.getOrElse(color, "\u001b[0m")
    println(s"$colorCode ∧,,,∧")
    println(s"$colorCode ( ̳- · -̳)")
    println(s"$colorCode/ づmehづ")
    println("\u001b[0m")
  }

  def welcomeMessage(): Unit = {
    println(Console.MAGENTA + "\nWelcome to the Kitty Card Game!")
    println("Players take turns drawing and placing cards on the grid.")
    println("Earn points by placing cards on matching colors or white squares." + Console.RESET)
  }

  override def update(event: GameEvent): Unit = {
    event match {
      case PromptForPlayerName(player1, player2) =>
        promptForPlayerName()
      case PlayerTurn(playerName) =>
        println(Console.BLUE + s"\n$playerName's turn.\n" + Console.RESET)
      case CardDrawn(playerName, card) =>
        println(Console.BLUE + s"\n$playerName drew: $card\n" + Console.RESET)
      case InvalidPlacement =>
        println("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
      case CardPlacementSuccess(x, y, card, points) =>
        println(Console.YELLOW + s"Card placed at ($x, $y): $card. Points earned: $points." + Console.RESET)
      case GameOver(player1Name, player1Points, player2Name, player2Points) =>
        println("Game over!")
        println(s"$player1Name's final score: $player1Points")
        println(s"$player2Name's final score: $player2Points")
        if (player1Points > player2Points) {
          println(s"$player1Name wins!")
        } else if (player2Points > player1Points) {
          println(s"$player2Name wins!")
        } else {
          println("It's a tie!")
        }
    }
  }
}
// src/main/scala/aview/Tui.scala
package aview

import model.patterns.{PreSeenDeckStrategy, RandomStrategy}
import util.*

import scala.io.StdIn.readLine

class Tui extends Observer {

  private[aview] val colors = Map(
    "Green" -> "\u001b[32m",
    "Brown" -> "\u001b[33m",
    "Purple" -> "\u001b[35m",
    "Blue" -> "\u001b[34m",
    "Red" -> "\u001b[31m",
    "White" -> "\u001b[37m"
  )

  private var undoCallback: () => Unit = () => {}
  private var redoCallback: () => Unit = () => {}

  def setUndoCallback(callback: () => Unit): Unit = {
    undoCallback = callback
  }

  def setRedoCallback(callback: () => Unit): Unit = {
    redoCallback = callback
  }

  def run(): Unit = {
    welcomeMessage()
    val gameMode = selectGameMode()

    gameMode match {
      case "Single" =>
        val singlePlayerOption = selectSinglePlayerOption()
        val playerName = promptPlayerName("Enter your name: ")
        println(s"Starting $singlePlayerOption mode for $playerName...")

      case "Multiplayer" =>
        val player1Name = promptPlayerName("Enter name for Player 1: ")
        val player2Name = promptPlayerName("Enter name for Player 2: ")
        println(s"Starting multiplayer game between $player1Name and $player2Name...")
        displayMultiplayerMenu()

      case _ =>
        println("An unexpected mode was selected.")
    }
  }

  def selectGameMode(): String = {
    println("Select game mode:")
    println("1. Single Player")
    println("2. Multiplayer")
    print("Enter the number corresponding to your choice: ")

    readLine().trim match {
      case "1" => "Single"
      case "2" => "Multiplayer"
      case _ =>
        println("Invalid choice, defaulting to Single Player mode.")
        "Single"
    }
  }

  def selectSinglePlayerOption(): String = {
    println("\nChoose an option for Single Player mode:")
    println("1. Feed the kitties")
    println("2. Play with the Kitty Card Boss")
    print("Enter the number corresponding to your choice: ")

    readLine().trim match {
      case "1" => "Feed the kitties"
      case "2" => "Play with the Kitty Card Boss"
      case _ =>
        println("Invalid choice, defaulting to 'Feed the kitties'.")
        "Feed the kitties"
    }
  }

  private def promptPlayerName(prompt: String): String = {
    print(prompt)
    readLine().trim
  }

  private def printColoredCat(color: String): Unit = {
    println(s"$color ∧,,,∧")
    println(s"$color( ̳• · •̳)")
    println(s"$color/    づ♡")
    println("\u001b[0m") // Reset color after printing
  }

  def printCatLoop(): Unit = {
    for (color <- colors.values) {
      printColoredCat(color)
      Thread.sleep(500) // Optional: Delay for visual effect
    }
  }

  private def printColoredMessage(color: String, message: String): Unit = {
    val colorCode = colors.getOrElse(color, "\u001b[0m")
    println(s"$colorCode ∧,,,∧")
    println(s"$colorCode ( ̳- · -̳)")
    println(s"$colorCode/ づ$message づ")
    println("\u001b[0m")
  }

  def printBadChoice(color: String): Unit = {
    printColoredMessage(color, "bad choice")
  }

  def printMeh(color: String): Unit = {
    printColoredMessage(color, "meh")
  }

  def welcomeMessage(): Unit = {
    println(Console.MAGENTA + "\nWelcome to the Kitty Card Game!")
    println("Players take turns drawing and placing cards on the grid.")
    println("Earn points by placing cards on matching colors or white squares." + Console.RESET)
  }

  def displayMultiplayerMenu(): Unit = {
    var continue = true
    while (continue) {
      println("Multiplayer Menu:")
      println("1. Place Card")
      println("2. Remove Card")
      println("3. Undo")
      println("4. Redo")
      println("5. Exit")
      print("Enter your choice: ")

      readLine().trim match {
        case "1" => placeCard()
        case "2" => removeCard()
        case "3" => undoCallback()
        case "4" => redoCallback()
        case "5" => continue = false
        case _ => println("Invalid choice. Please try again.")
      }
    }
  }

  def selectStrategy(): Unit = {
    println("Choose a strategy for Multiplayer mode:")
    println("1. Random Strategy")
    println("2. Pre-Seen Deck Strategy")
    print("Enter the number corresponding to your choice: ")

    val strategy = readLine().trim match {
      case "1" => new RandomStrategy()
      case "2" => new PreSeenDeckStrategy()
      case _ =>
        println("Invalid choice, defaulting to Random Strategy.")
        new RandomStrategy()
    }

    // Hier können Sie die Strategie an den GameController zurückgeben oder weiterverarbeiten
  }

  def placeCard(): Unit = {
    // Implement the logic to place a card
  }

  def removeCard(): Unit = {
    // Implement the logic to remove a card
  }

  override def update(event: GameEvent): Unit = {
    event match {
      case PlayerTurn(playerName) =>
        println(Console.BLUE + s"\n$playerName's turn." + Console.RESET)
      case CardDrawn(playerName, card) =>
        println(Console.BLUE + s"$playerName drew: $card\n" + Console.RESET)
      case InvalidPlacement() =>
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
      case TotalPoints(player1Points, player2Points) =>
        println(s"Total points: Player 1: $player1Points, Player 2: $player2Points")
      case UndoEvent(_) => println("Undo performed.")
      case RedoEvent(_) => println("Redo performed.")
      case util.PrintMeh(_) => print("meh not implemented yet")
      case util.PrintBadChoice(_) => print("bad not implemented yet")
      case util.ShowColoredCat(_) => print("show cat not implemented yet")
      
    }
  }
}
package aview

import controller.GameController
import scalafx.application.Platform
import util._

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
    println("\u001b[0m")
  }

  def printCatLoop(): Unit = {
    for (color <- colors.values) {
      printColoredCat(color)
      Thread.sleep(500)
    }
  }

  def processInput(input: String): Unit = {
    input.trim.toLowerCase match {
      case "undo" => gameController.handleCommand("undo")
      case "redo" => gameController.handleCommand("redo")
      case "draw" => gameController.handleCommand("draw")
      case input if input.matches("\\d+\\s+\\d+\\s+\\d+") =>
        val parts = input.split(" ")
        gameController.handleCardPlacement(parts(0).toInt, parts(1).toInt, parts(2).toInt)
      case _ =>
        println("Invalid input! Please use one of the following formats:")
        println("- 'draw' to draw a card")
        println("- 'cardIndex x y' to place a card")
        println("- 'undo' to undo last move")
        println("- 'redo' to redo last move")
    }
  }

  def promptForPlayerName(): Unit = {
    Platform.runLater {
      val gui = new NameInputApp((player1, player2) => {
        println(s" $player1, Last Name: $player2")
        gameController.promptForPlayerName(player1, player2)
      })
      gui.show()
    }
  }

  private var skipPrompt = false

  def skipNamePrompt(): Unit = {
    skipPrompt = true
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

  def printGridColors(): Unit = {
    val colors = gameController.getGridColors
    colors.foreach { case (x, y, card, color) =>
      val cardInfo = card.map(_.toString).getOrElse("Empty")
      println(s"Rectangle at ($x, $y) has card: $cardInfo and color: $color")
    }
  }

  override def update(event: GameEvent): Unit = {
    event match {
      case UpdatePlayers(player1, player2) =>
      //print("\n", player1, player2)
      case PlayerTurn(playerName) =>
        println(Console.BLUE + s"\n$playerName's turn.\n" + Console.RESET)
        val input = inputProvider.getInput
        processInput(input)
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
      case updateGrid(grid) =>
        printGridColors()
      case UndoEvent(_) => println("Undo performed.")
      case RedoEvent(_) => println("Redo performed.")
      case ShowCardsForPlayer(cards) =>
        cards.foreach(println)
      case UpdatePlayer(player1) => print(player1)
      case PromptForPlayerName => promptForPlayerName()
      case _ => println("Invalid event.")
    }
  }
}
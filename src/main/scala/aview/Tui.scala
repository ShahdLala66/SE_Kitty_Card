package aview

import controller.GameControllerInterface
import util.*

import java.lang.Thread.sleep

class Tui(gameController: GameControllerInterface) extends Observer {
  gameController.add(this)

  var inputProvider: InputProvider = new ConsoleProvider

  override def update(event: GameEvent): Unit = {
    event match {
      case GameSaved =>
        println("Game saved successfully")
      case UpdateLoadedGame(gridColors, currentPlayer, p1, p2, hand) =>
        println("Game loaded successfully")
        printGridColors()
        println(s"\nCurrent players: ${p1.name} vs ${p2.name}")
        println(s"\n${currentPlayer.name}'s turn!")
        println("\nYour cards:")
        hand.foreach(println)

      case UpdatePlayers(player1, player2) =>
        inputProvider.interrupt()
        inputProvider.interrupt()
        flush()
        println(Console.YELLOW + "The Players are " + player1.name + " and " + player2.name)
        println("Let's start the game!" + Console.RESET)
        inputProvider.interrupt()

      case PlayerTurn(playerName) =>
        inputProvider.interrupt()
        val input = inputProvider.getInput
        if (input != null) {
          processInput(input)
        } else {
          gameController.askForInputAgain()
        }
        inputProvider.interrupt()

      case CardDrawn(playerName, card) =>
        println(Console.BLUE + s"\n$playerName drew: $card\n" + Console.RESET)

      case InvalidPlacement =>
        flush()
        println(Console.RED + "Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.\n" + Console.RESET)
       // println(Console.BLUE + s"\nIt's ${gameController.getCurrentplayer.getPlayerName}'s turn!\n" + Console.RESET)
      //  printGridColors()
        inputProvider.interrupt()

      case CardPlacementSuccess(x, y, card, points, player) =>
        println(Console.YELLOW + s"\nCard placed at ($x, $y): $card. You ($player) have earned $points points!" + Console.RESET)

      case GameOver(player1Name, player1Points, player2Name, player2Points) =>
        println(Console.RED + "\n\nGame over!")
        println(Console.YELLOW + s"$player1Name's final score: $player1Points")
        println(s"$player2Name's final score: $player2Points")
        if (player1Points > player2Points) {
          println(s"$player1Name wins!" + Console.RESET)
        } else if (player2Points > player1Points) {
          println(s"$player2Name wins!" + Console.RESET)
        } else {
          println("It's a tie!" + Console.RESET)
        }

      case UpdateGrid(grid) =>
        printGridColors()

      case UndoEvent(_) =>
        println(Console.RED + "\nUndo performed." + Console.RESET)
        printGridColors()
        inputProvider.interrupt()

      case RedoEvent(_) =>
        println(Console.RED + "\nRedo performed." + Console.RESET)
        printGridColors()
        inputProvider.interrupt()


      case ShowCardsForPlayer(cards) =>
        println("\nYour cards:")
        cards.foreach(println)

      case PromptForPlayerName =>
        inputProvider.interrupt()
        // flush()
        println(s"Enter the name for Player 1:")
        val player1 = inputProvider.getInput
        if (player1 == null) return
        inputProvider.interrupt()

        println(s"Enter the name for Player 2:")
        val player2 = inputProvider.getInput
        if (player2 == null) return
        inputProvider.interrupt()

        gameController.promptForPlayerName(player1, player2)

      case UpdatePlayer(player1) =>
        updateStatus(s"$player1's turn.")

      case AskForGameMode =>
        inputProvider.interrupt()
        println(s"Single- or Multiplayer (s/m):")
        val mode = inputProvider.getInput
        if (mode == null) return
        gameController.setGameMode(mode)
        inputProvider.interrupt()

      case AskForLoadGame =>
        inputProvider.interrupt()
        println("Would you like to:")
        println("(1) Start new game")
        println("(2) Load saved game")
        val choice = inputProvider.getInput
        if (choice == null) return
        choice match {
          case "2" => gameController.handleCommand("load")
          case _ => gameController.handleCommand("start")
        }
      case GameLoaded(grid, currentPlayer, player1, player2, currentPlayerHand) => print("")
        inputProvider.interrupt()

      case _ => println("Invalid event.")
    }
  }

  private def updateStatus(message: String): Unit = {
    println(Console.BLUE + message + Console.RESET)
  }

  private[aview] val colors = Map(
    "Green" -> "\u001b[32m",
    "Brown" -> "\u001b[33m",
    "Purple" -> "\u001b[35m",
    "Blue" -> "\u001b[34m",
    "Red" -> "\u001b[31m",
    "White" -> "\u001b[37m"
  )

  def printColoredCat(color: String): Unit = {
    println(s"$color ∧,,,∧")
    println(s"$color( ̳• · •̳)")
    println(s"$color/    づ♡")
    println("\u001b[0m")
  }

  def flush(): Unit = {
    print("\n" * 25)
  }

  def printCatLoop(): Unit = {
    for (color <- colors.values) {
      printColoredCat(color)
      Thread.sleep(500)
    }
  }

  def processInput(input: String): Unit = {
    if (input == null) throw new IllegalArgumentException("Input cannot be null")
    gameController.askForInputAgain()
    input.trim.toLowerCase match {
      case "undo" =>
        gameController.handleCommand("undo")
        gameController.askForInputAgain()
      case "redo" =>
        gameController.handleCommand("redo")
        gameController.askForInputAgain()
      case "draw" =>
        gameController.handleCommand("draw")
        gameController.askForInputAgain()
      case "save" =>
        gameController.handleCommand("save")
        gameController.askForInputAgain()
      case "load" =>
        gameController.handleCommand("load")
        gameController.askForInputAgain()
      case input if input.matches("\\d+\\s+\\d+\\s+\\d+") =>
        val parts = input.split(" ")
        if (parts.length != 3) throw new IllegalArgumentException("Invalid input format for card placement")
        gameController.handleCardPlacement(parts(0).toInt, parts(1).toInt, parts(2).toInt)
      case _ =>
        println("Invalid input! Please use one of the following formats:")
        println("- 'draw' to draw a card")
        println("- 'cardIndex x y' to place a card")
        println("- 'undo' to undo last move")
        println("- 'redo' to redo last move")
        gameController.askForInputAgain()
    }
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

  private def printGridColors(): Unit = {
    val colors = gameController.getGridColors
    colors.foreach { case (x, y, card, color) =>
      val cardInfo = card.map(_.toString).getOrElse("Empty")
      println(s"Rectangle at ($x, $y) has card: $cardInfo and color: $color")
    }
    println()
  }
}
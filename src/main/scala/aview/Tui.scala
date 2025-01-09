package aview

import controller.GameControllerInterface
import util.*

class Tui(gameController: GameControllerInterface) extends Observer {
    gameController.add(this)

    private val inputProvider: InputProvider = new ConsoleProvider

    override def update(event: GameEvent): Unit = {
        event match {
            case PlayerTurn(playerName) =>
                val input = inputProvider.getInput
                processInput(input)
            case CardDrawn(playerName, card) =>
                println(Console.BLUE + s"\n$playerName drew: $card\n" + Console.RESET)
            case UpdatePlayer(player1) =>
                println(Console.BLUE + s"\nIt's $player1's turn!\n" + Console.RESET)
            case InvalidPlacement =>
                flush()
                println(Console.RED + "Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.\n" + Console.RESET)
                println(Console.BLUE + s"\nIt's ${gameController.getCurrentplayer.getPlayerName}'s turn!\n" + Console.RESET)
                printGridColors()
                gameController.showCardsForPlayer(gameController.getCurrentplayer.getHand)
            case CardPlacementSuccess(x, y, card, points) =>
                println(Console.YELLOW + s"\nCard placed at ($x, $y): $card. You have earned $points points!" + Console.RESET)
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
            case UndoEvent(_) => println("Undo performed.")
            case RedoEvent(_) => println("Redo performed.")
            case ShowCardsForPlayer(cards) =>
                cards.foreach(println)
            case PromptForPlayerName =>
                flush()
                println(s"Enter the name for Player 1:")
                val player1 = inputProvider.getInput
                if (player1 == null) return // Input was interrupted

                println(s"Enter the name for Player 2:")
                val player2 = inputProvider.getInput
                if (player2 == null) return // Input was interrupted

                gameController.promptForPlayerName(player1, player2)

            case UpdatePlayers(player1, player2) =>
                // Interrupt any pending input when players are updated through GUI
                inputProvider.interrupt()
                flush()
                println(Console.YELLOW + "The Players are " + player1.name + " and " + player2.name)
                println("Let's start the game!" + Console.RESET)

            case _ => println("Invalid event.")
        }
    }

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

    def flush(): Unit = {
        print("\n" * 25)
    }

    def printCatLoop(): Unit = {
        for (color <- colors.values) {
            printColoredCat(color)
            Thread.sleep(500)
        }
    }

    private def processInput(input: String): Unit = {
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
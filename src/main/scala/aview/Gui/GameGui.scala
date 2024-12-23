package aview.Gui

import controller.GameController
import model.cards.{Card, NumberCards, Value}
import util.*
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.stage.Stage

class GameGuiTui(gameController: GameController) extends Observer {
  private val inputProvider: InputProvider = new ConsoleProvider

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
    println(s"Enter the name for Player 1:")
    val player1 = inputProvider.getInput
    println(s"Enter the name for Player 2:")
    val player2 = inputProvider.getInput
    gameController.promptForPlayerName(player1, player2)
  }

  private var skipPrompt = false

  def skipNamePrompt(): Unit = {
    skipPrompt = true
  }

  def start(): Unit = {
    promptForPlayerName()
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
        println("\nYour cards:")
        cards.foreach(println)
        showCardsGUI(cards)
      case UpdatePlayer(player1) => print(player1)
      case PromptForPlayerName =>
      case _ => println("Invalid event.")
    }
  }

  def showCardsGUI(cards: Seq[Card]): Unit = {
    Platform.runLater {
      val cardImages = cards.map {
        case NumberCards(suit, value) =>
          val numericValue = value match {
            case Value.One => "1"
            case Value.Two => "2"
            case Value.Three => "3"
            case Value.Four => "4"
            case Value.Five => "5"
            case Value.Six => "6"
            case _ => throw new IllegalArgumentException(s"Unsupported card value: $value")
          }

          val germanSuit = suit.toString match {
            case "Red" => "Rot"
            case "Purple" => "Lila"
            case "Brown" => "Braun"
            case "Blue" => "Blau"
            case "Green" => "Grün"
            case _ => throw new IllegalArgumentException(s"Unsupported card suit: $suit")
          }

          CardImage(numericValue, germanSuit)
        case card =>
          throw new IllegalArgumentException(s"Unsupported card type: $card")
      }

      cardImages.foreach(cardImage => println(s"Generated imagePath: ${cardImage.imagePath}"))

      val cardButtons = cardImages.map(cardImage => {
        println(s"Creating button for: ${cardImage.value} of ${cardImage.suit}")
        new CardButton(cardImage, _ => {})
      })

      println("Creating stage and showing it...")
      val stage = new Stage {
        title = "Your Cards"
        scene = new Scene {
          content = new HBox {
            spacing = 10
            children = cardButtons
          }
        }
      }
      stage.show()
      println("Stage should be shown now.")
    }
  }
}
package aview.Gui

import controller.GameController
import model.cards.{Card, NumberCards, Value}
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.stage.Stage
import util.*

import java.util.concurrent.CountDownLatch

class GameGuiTui(gameController: GameController) extends Observer {
  private val inputProvider: InputProvider = new ConsoleProvider
  private var currentStage: Stage = _

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

  var toggle: Boolean = true

  override def update(event: GameEvent): Unit = {
    event match {
      case UpdatePlayers(player1, player2) =>
      case PlayerTurn(playerName) =>
        println(Console.BLUE + s"\n$playerName's turn.\n" + Console.RESET)
        while toggle do {
          val input = inputProvider.getInput
          processInput(input)
        }
        toggle = true

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
        toggle = false
        cards.foreach(println)
        showCardsGUI(cards)
        toggle = true
      case UpdatePlayer(player1) => print(player1)
      case PromptForPlayerName =>
      case _ => println("Invalid event.")
    }
  }


  def showCardsGUI(cards: Seq[Card]): Unit = {
    println("Starting showCardsGUI method")

    // Ensure JavaFX is initialized
    GuiInitializer.ensureInitialized()
    println("JavaFX initialization check complete")

    if (!Platform.isFxApplicationThread) {
      println("Not on JavaFX thread, scheduling UI update")
    }

    Platform.runLater {
      try {
        println("Inside Platform.runLater")

        // Close existing stage if it exists
        Option(currentStage).foreach(_.close())

        println("Processing cards...")
        val cardImages = cards.map {
          case NumberCards(suit, value) =>
            println(s"Processing card: $suit $value")
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

            println(s"Created CardImage for $numericValue of $germanSuit")
            CardImage(numericValue, germanSuit)
          case card =>
            throw new IllegalArgumentException(s"Unsupported card type: $card")
        }

        println("Creating card buttons...")
        val cardButtons = cardImages.map(cardImage => {
          println(s"Creating button for ${cardImage.value} of ${cardImage.suit}")
          new CardButton(cardImage, _ => {})
        })

        println("Creating new stage...")
        currentStage = new Stage {
          title = "Your Cards"
          scene = new Scene {
            root = new HBox {
              spacing = 10
              children = cardButtons
            }
          }

          onCloseRequest = _ => println("Stage is being closed")
        }

        println("Showing stage...")
        currentStage.show()
        println("Stage shown successfully")

      } catch {
        case e: Exception =>
          println(s"Error in GUI creation: ${e.getMessage}")
          e.printStackTrace()
      }
    }
  }


}

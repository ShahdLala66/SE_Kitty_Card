package aview.Gui

import controller.GameController
import model.cards.{Card, NumberCards, Value}
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.stage.Stage
import util.*

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
  var toggle = false

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

  private var currentStage: Stage = _
  private var cardPane: HBox = _
  private var gridPane: GridPane = _
  private var statusLabel: Label = _ // Status label for feedback
  private var controlPane: HBox = _ // New pane for additional controls
  private var selectedCardIndex: Option[Int] = None

  def initialize(): Unit = {
    GuiInitializer.ensureInitialized()

    Platform.runLater {
      // Initialize status label
      statusLabel = new Label {
        text = "Welcome to the game!"
        style = "-fx-font-size: 14px;"
      }

      // Initialize control pane with additional buttons
      controlPane = new HBox {
        spacing = 10
        padding = Insets(10)
        alignment = Pos.Center
        children = Seq(
          new Button("Undo") {
            onAction = _ => gameController.handleCommand("undo")
          },
          new Button("Redo") {
            onAction = _ => gameController.handleCommand("redo")
          },
          new Button("Draw Card") {
            onAction = _ => gameController.handleCommand("draw")
          }
        )
      }

      cardPane = new HBox {
        spacing = 10
        padding = Insets(10)
        alignment = Pos.Center
      }

      gridPane = createGrid()

      currentStage = new Stage {
        title = "Game Display"
        scene = new Scene {
          root = new VBox {
            spacing = 20
            padding = Insets(10)
            alignment = Pos.Center
            children = Seq(
              statusLabel, // Add status label at the top
              new VBox {
                alignment = Pos.Center
                children = Seq(gridPane)
              },
              controlPane, // Add control pane
              new VBox {
                alignment = Pos.Center
                children = Seq(cardPane)
              }
            )
          }
        }
        width = 500
        height = 700 // Increased height to accommodate new elements
      }
      currentStage.show()
    }
  }

  def createGrid(): GridPane = {
    val grid = new GridPane {
      hgap = 5
      vgap = 5
      padding = Insets(10)
      alignment = Pos.Center
    }

    val colors = gameController.getGridColors
    for ((x, y, card, color) <- colors) {
      val buttonText = card.map(_.toString).getOrElse(s"($x, $y)")
      val button = new Button(buttonText) {
        style = s"-fx-background-color: $color;"
        prefWidth = 80
        prefHeight = 80

        onAction = _ => handleGridClick(x, y)

        // Add hover effect
        onMouseEntered = _ => {
          if (selectedCardIndex.isDefined) {
            style = s"-fx-background-color: $color; -fx-opacity: 0.8;"
          }
        }
        onMouseExited = _ => {
          style = s"-fx-background-color: $color;"
        }
      }
      grid.add(button, x, y)
    }
    grid
  }

  private def handleGridClick(x: Int, y: Int): Unit = {
    selectedCardIndex match {
      case Some(cardIndex) =>
        // Try to place the card
        updateStatus(s"Attempting to place card at position ($x, $y)")
        gameController.handleCardPlacement(cardIndex, x, y)
        selectedCardIndex = None
        updateButtonStyles()
      case None =>
        updateStatus("Please select a card first!")
    }
  }

  // Method to update status label
  def updateStatus(message: String): Unit = {
    Platform.runLater {
      if (statusLabel != null) {
        statusLabel.text = message
      }
    }
  }

  private def updateButtonStyles(): Unit = {
    cardPane.children.foreach { node =>
      node.asInstanceOf[Button].style = ""
    }
  }

  def showCardsGUI(cards: Seq[Card]): Unit = {
    Platform.runLater {
      try {
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

            CardImage(numericValue, germanSuit)
        }
        val cardButtons = cardImages.zipWithIndex.map { case (cardImage, index) =>
          println(s"Creating button for ${cardImage.value} of ${cardImage.suit}")
          new CardButton(cardImage, _ => {
            selectedCardIndex = Some(index)
            updateButtonStyles()
            updateStatus(s"Selected card: $cardImage")
          })
        }

        if (cardPane != null) {
          cardPane.children = cardButtons
        }
        updateDisplay()

      } catch {
        case e: Exception =>
          println(s"Error in GUI creation: ${e.getMessage}")
          e.printStackTrace()
      }
    }
  }


  def updateDisplay(): Unit = {
    if (currentStage == null) {
      initialize()
    }
  }

  def showGrid(): Unit = {
    Platform.runLater {
      if (gridPane != null) {
        val newGrid = createGrid()
        gridPane.children.clear()
        gridPane.children.addAll(newGrid.children)
      }
      updateDisplay()
    }
  }

  // Rest of your update method remains the same
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
        showGrid()
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
}

// Add a variable to store the selected card index
//  private var selectedCardIndex: Option[Int] = None
//
//  // Update the CardButton to set the selected card index when clicked
//  val cardButtons = cardImages.zipWithIndex.map { case (cardImage, index) =>
//    new CardButton(cardImage, _ => {
//      selectedCardIndex = Some(index)
//      println(s"Selected card index: $index")
//    })

// Modify the SubmitButton's onAction to use the selected card index
//  val SubmitButton = new Button {
//    text = "Submit"
//    onAction = _ => {
//      selectedCardIndex match {
//        case Some(index) =>
//          val input = s"$index x y" // Replace x and y with actual values
//          processInput(input)
//        case None =>
//          println("No card selected")
//      }
//      toggle = false
//    }
//  }



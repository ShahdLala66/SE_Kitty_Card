package aview.Gui

import controller.GameController
import model.cards.{Card, NumberCards, Value}
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.stage.{Modality, Stage}
import util.*
import scalafx.scene.paint.Color

class GameGuiTui(gameController: GameController) extends Observer {
  private val inputProvider: InputProvider = new ConsoleProvider
  private var currentStage: Stage = _
  private var cardPane: HBox = _
  private var gridPane: GridPane = _
  private var statusLabel: Label = _
  private var controlPane: HBox = _
  private var selectedCardIndex: Option[Int] = None


  private var isWaitingForNames = false
  private var nameDialogStage: Option[Stage] = None

  // Modify the promptForPlayerName method
  def promptForPlayerName(onComplete: (String, String) => Unit): Unit = {
    if (!isWaitingForNames) {
      isWaitingForNames = true
      Platform.runLater {
        val dialog = new Stage {
          title = "Player Names"
          scene = new Scene {
            val player1Field = new TextField {
              promptText = "Player 1 Name"
            }
            val player2Field = new TextField {
              promptText = "Player 2 Name"
            }
            val submitButton = new Button("Start Game") {
              onAction = _ => {
                if (player1Field.text.value.nonEmpty && player2Field.text.value.nonEmpty) {
                  isWaitingForNames = false
                  close()
                  onComplete(player1Field.text.value, player2Field.text.value)
                }
              }
            }

            root = new VBox(10) {
              padding = Insets(20)
              alignment = Pos.Center
              children = Seq(
                new Label("Enter Player Names"),
                player1Field,
                player2Field,
                submitButton
              )
            }
          }
          initModality(Modality.APPLICATION_MODAL)

          // Add handler for when dialog is closed directly (X button)
          onCloseRequest = _ => {
            isWaitingForNames = false
          }
        }
        nameDialogStage = Some(dialog)
        dialog.showAndWait()
      }
    }
  }

  // Add this method to close the name dialog if it's open
  def closeNameDialog(): Unit = {
    Platform.runLater {
      nameDialogStage.foreach { dialog =>
        dialog.close()
        isWaitingForNames = false
      }
      nameDialogStage = None
    }
  }


  def start(): Unit = {
    GuiInitializer.ensureInitialized()
    promptForPlayerName { (player1Name, player2Name) =>
      gameController.promptForPlayerName(player1Name, player2Name)
      initialize()
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
        println("Invalid input!")
    }
  }

  def initialize(): Unit = {
    Platform.runLater {
      statusLabel = new Label {
        text = "Welcome to the game!"
        style = "-fx-font-size: 14px;"
      }

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
              statusLabel,
              new VBox {
                alignment = Pos.Center
                children = Seq(gridPane)
              },
              controlPane,
              new VBox {
                alignment = Pos.Center
                children = Seq(cardPane)
              }
            )
          }
        }
        width = 500
        height = 700
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
        updateStatus(s"Attempting to place card at position ($x, $y)")
        gameController.handleCardPlacement(cardIndex, x, y)
        selectedCardIndex = None
        updateButtonStyles()
      case None =>
        updateStatus("Please select a card first!")
    }
  }

  def updateStatus(message: String): Unit = {
    Platform.runLater {
      if (statusLabel != null) {
        statusLabel.text = message
      }
    }
  }

  // Fixed updateButtonStyles method
  private def updateButtonStyles(): Unit = {
    if (cardPane != null) {
      cardPane.children.foreach { node =>
        node match {
          case stackPane: javafx.scene.layout.StackPane =>
            // Reset the style for StackPane (CardButton)
            stackPane.setStyle("")
          case button: javafx.scene.control.Button =>
            // Reset the style for regular buttons
            button.setStyle("")
          case _ => // Do nothing for other types
        }
      }
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

  def updateDisplay(): Unit = {
    if (currentStage == null) {
      initialize()
    }
  }

  def invalidPlacements(): Unit = {
    updateStatus("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
  }

  def CardPlacementSuccesss(x: Int, y: Int, card: String, points: Int): Unit = {
    updateStatus(s"Card placed at ($x, $y): $card. Points earned: $points.")
  }

  def PlayerTurs(playerName: String): Unit = {
    updateStatus(s"$playerName's turn.")
  }

  def CardDrawns(playerName: String, card: String): Unit = {
    updateStatus(s"$playerName drew: $card")
  }

  override def update(event: GameEvent): Unit = {
    event match {
      case UpdatePlayers(player1, player2) => closeNameDialog()  // Close the GUI name dialog if it's open
      case PlayerTurn(playerName) =>
        PlayerTurs(playerName)
        val input = inputProvider.getInput
      case CardDrawn(playerName, card) =>
        CardDrawns(playerName, card)
      case InvalidPlacement =>
        invalidPlacements()
      case CardPlacementSuccess(x, y, card, points) =>
        CardPlacementSuccesss(x, y, card, points)
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
        showGrid()
      case UndoEvent(_) => println("Undo performed.")
      case RedoEvent(_) => println("Redo performed.")
      case ShowCardsForPlayer(cards) =>
        cards.foreach(println)
        showCardsGUI(cards)
      case UpdatePlayer(player1) => print(player1)
      case PromptForPlayerName =>
        if (!isWaitingForNames) {start()}
      case _ => println("Invalid event.")
    }
  }
}
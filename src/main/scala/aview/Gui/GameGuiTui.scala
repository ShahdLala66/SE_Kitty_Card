package aview.Gui

import controller.GameControllerInterface
import model.cardComp.CardInterface
import model.cardComp.baseImp.{NumberCards, Value}
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{AnchorPane, GridPane, HBox, Region, StackPane, VBox}
import scalafx.scene.text.Font
import scalafx.stage.{Modality, Stage}
import util.*

import scala.swing.MenuBar.NoMenuBar.font

class GameGuiTui(gameController: GameControllerInterface) extends Observer {
    gameController.add(this)
    val bubblegumSans: Font = Font.loadFont(getClass.getResourceAsStream("/BubblegumSans-Regular.ttf"), 20)
    private var currentStage: Stage = _
    private var cardPane: HBox = _
    private var gridPane: GridPane = _
    private var statusLabel: Label = _
    private var controlPane: HBox = _
    private var selectedCardIndex: Option[Int] = None


    private var nameDialogStage: Option[Stage] = None

    // Modify the promptForPlayerName method
    def promptForPlayerName(onComplete: (String, String) => Unit): Unit = {
        Platform.runLater {
            val dialog = new Stage {
                title = "Player Names"
                scene = new Scene(500, 400) {
                    val player1Field = new TextField {
                        promptText = "Player 1 Name"
                        font = bubblegumSans
                        style = "-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;"
                        prefWidth = 300
                        prefHeight = 50
                        alignment = Pos.Center
                    }

                    val player2Field = new TextField {
                        promptText = "Player 2 Name"
                        font = bubblegumSans
                        style = "-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;"
                        prefWidth = 300
                        prefHeight = 50
                        alignment = Pos.Center
                    }

                    val submitButton = new Button {
                        text = "Start Game"
                        minWidth = 500
                        minHeight = 55
                        style = "-fx-background-image: url('file:src/main/resources/Submit.png');" +
                            "-fx-background-color: transparent; -fx-background-size: cover;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-insets: 0;" +
                            "-fx-padding: 0;"
                        font = bubblegumSans
                        onAction = _ => {
                            if (player1Field.text.value.nonEmpty && player2Field.text.value.nonEmpty) {
                                close()
                                onComplete(player1Field.text.value, player2Field.text.value)
                            }
                        }
                    }

                    val player1FieldWithImage = new StackPane {
                        children = Seq(
                            new ImageView(new Image("file:src/main/resources/TextField.png")) {
                                fitWidth = 350
                                fitHeight = 50
                            },
                            player1Field
                        )
                        alignment = Pos.Center
                        prefWidth = 350
                        prefHeight = 50
                    }

                    val player2FieldWithImage = new StackPane {
                        children = Seq(
                            new ImageView(new Image("file:src/main/resources/TextField.png")) {
                                fitWidth = 350
                                fitHeight = 50
                            },
                            player2Field
                        )
                        alignment = Pos.Center
                        prefWidth = 350
                        prefHeight = 50
                    }

                    root = new VBox(20) {
                        padding = Insets(20)
                        alignment = Pos.Center
                        style = "-fx-background-image: url('file:src/main/resources/NameBackground.png'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-position: center; " +
                            "-fx-background-repeat: no-repeat;"
                        children = Seq(
                            new Label("Enter Player Names") {
                                font = bubblegumSans // Explicitly set the font
                                style = "-fx-font-size: 22px; -fx-text-fill: black; -fx-font-family: 'Bubblegum Sans';" // Ensure no conflicts
                            },
                            player1FieldWithImage,
                            player2FieldWithImage,
                            submitButton
                        )
                    }
                }
                initModality(Modality.APPLICATION_MODAL)
            }
            nameDialogStage = Some(dialog)
            dialog.showAndWait()
        }
    }

    // Add this method to close the name dialog if it's open
    def closeNameDialog(): Unit = {
        Platform.runLater {
            nameDialogStage.foreach { dialog =>
                dialog.close()
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
                font = bubblegumSans
                style = "-fx-font-size: 22px; -fx-text-fill: black; -fx-font-family: 'Bubblegum Sans';"
            }

            // Create the basic control pane first
            controlPane = new HBox { // Create a new HBox to hold the buttons
                spacing = 10
                padding = Insets(50, 0, 0, 20)
                alignment = Pos.Center
                children = Seq(
                    new Button("Undo") {
                        onAction = * => gameController.handleCommand("undo")
                    },
                    new Button("Redo") {
                        onAction = * => gameController.handleCommand("redo")
                    },
                    new Button("Draw Card") {
                        onAction = _ => gameController.handleCommand("draw")
                    }
                )
            }

            cardPane = new HBox { // Create a new HBox to hold the cards
                spacing = 10
                padding = Insets(35, 0, 0, 20)
                alignment = Pos.BottomCenter
            }

            gridPane = createGrid()
            gridPane.padding = Insets(60, 0, 0, 0) // Add padding to move the grid down


            // Setup GIF
            val gifPath = getClass.getResource("/ZayneChillingGif.gif")
            if (gifPath == null) {
                throw new IllegalStateException("GIF file not found in resources")
            }

            val image = new Image(gifPath.toExternalForm)
            val imageView = new ImageView(image) {
                fitWidth = 150
                fitHeight = 150
                preserveRatio = true
                mouseTransparent = true // Ensure it doesn't block interactions
            }

            // Create an AnchorPane to hold both controls and GIF
            val controlWithGifPane = new AnchorPane {
                children = Seq(controlPane, imageView)

                // Position the control pane
                AnchorPane.setTopAnchor(controlPane, 0.0) // Adjust vertical position of the control pane (upwards) of the GIF
                AnchorPane.setLeftAnchor(controlPane, 0.0)

                // Position the GIF
                AnchorPane.setTopAnchor(imageView, -100.0) // Adjust vertical position
                AnchorPane.setLeftAnchor(imageView, 300.0) // Position it right of the Draw button
            }

            val rootPane = new VBox { // Create a new VBox to hold all the elements
                spacing = 20
                padding = Insets(10)
                alignment = Pos.TopCenter
                children = Seq(
                    statusLabel,
                    new VBox {
                        children = Seq(gridPane)
                    },
                    controlWithGifPane, // Use the new AnchorPane instead of controlPane
                    new VBox {
                        children = Seq(cardPane)
                    }
                )

                style = "-fx-background-image: url('file:src/main/resources/Grid.png'); " +
                  "-fx-background-size: 444px 695px; " +
                  "-fx-background-position: center;"
            }

            currentStage = new Stage {
                title = "Kitty Cardsss"
                scene = new Scene {
                    root = rootPane
                }
                width = 444
                height = 750
            }
            currentStage.show()
        }
    }
    def createGrid(): GridPane = {
        val grid = new GridPane {
            hgap = 6
            vgap = 6
            padding = Insets(81, 0, 0, 0) // Oben, Rechts, Unten, Links
            alignment = Pos.Center
        }

        val colors = gameController.getGridColors
        for ((x, y, card, color) <- colors) {
            val buttonText = card.map(_.toString).getOrElse(s"($x, $y)")
            val button = new Button(buttonText) {
                style = s"-fx-background-color: $color; -fx-opacity: 0.5;"
                prefWidth = 91
                prefHeight = 90
                onAction = _ => handleGridClick(x, y)
                onMouseEntered = _ => {
                    if (selectedCardIndex.isDefined) {
                        style = s"-fx-background-color: $color; -fx-opacity: 0.8;"
                    }
                }
                onMouseExited = _ => {
                    style = s"-fx-background-color: $color; -fx-opacity: 0.5;"
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

    def showCardsGUI(cards: Seq[CardInterface]): Unit = {
        Platform.runLater {
            try {
                // println("Processing cards...")
                val cardImages = cards.map {
                    case NumberCards(suit, value) =>
                        // println(s"Processing card: $suit $value")
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
                    // println(s"Creating button for ${cardImage.value} of ${cardImage.suit}")
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
                    // println(s"Error in GUI creation: ${e.getMessage}")
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
            case UpdatePlayers(player1, player2) => closeNameDialog() // Close the GUI name dialog if it's open
            case PlayerTurn(playerName) =>
                PlayerTurs(playerName)
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
            case UpdateGrid(grid) =>
                showGrid()

            case UndoEvent(_) => showGrid()
                updateDisplay()
            // showCardsGUI(cards)
            case RedoEvent(_) => showGrid()
                updateDisplay()
            // showCardsGUI(cards)
            case ShowCardsForPlayer(cards) =>
                showCardsGUI(cards)
            case UpdatePlayer(player1) =>
            case PromptForPlayerName => start()
            case _ => println("Invalid event.")
        }
    }
}
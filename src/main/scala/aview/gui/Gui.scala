package aview.gui

import controller.GameControllerInterface
import model.CardInterface
import model.baseImp.Suit.Suit
import model.baseImp.{NumberCards, Player, Value}
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.*
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.text.{Font, Text}
import scalafx.stage.{Modality, Stage}
import util.*

class Gui(gameController: GameControllerInterface) extends Observer {
  gameController.add(this)

  private val bubblegumSans: Font = Font.loadFont(getClass.getResourceAsStream("/assets/fonds/BubblegumSans-Regular.ttf"), 20)
  private var currentStage: Stage = _
  private var cardPane: HBox = _
  private var gridPane: GridPane = _
  private var statusLabel: Label = _
  private var controlPane: HBox = _
  private var selectedCardIndex: Option[Int] = None
  private var nameDialogStage: Option[Stage] = None


  override def update(event: GameEvent): Unit = {
    event match {
      case AskForLoadGame =>
        showStartOrLoadGameWindow { choice =>
          gameController.handleCommand(choice)
        }
      case InitializeGUIForLoad =>
      //GuiInitializer.ensureInitialized()
      case UpdateLoadedGame(gridColors, currentPlayer, p1, p2, hand) =>
        updateDisplay()
        updateGridDisplay(gridColors)
        updatePlayerDisplay(p1, p2)
        updateCurrentPlayerStatus(currentPlayer)
        showCardsGUI(hand)
      case MakeNewGame =>
      //  GuiInitializer.ensureInitialized()
      //updateDisplay()
      //  initialize()
      //updateDisplay() // Initialize the GUI
      case UpdatePlayers(player1, player2) => closeNameDialog()
        updateDisplay() //wichtig
      case PlayerTurn(playerName) =>
        PlayerTurs(playerName)
        showCardsGUI(gameController.getCurrentplayer.getHand)
      case CardDrawn(playerName, card) =>
        CardDrawns(playerName, card)
      case InvalidPlacement =>
        invalidPlacements()
      case CardPlacementSuccess(x, y, card, points) =>
        CardPlacementSuccesss(x, y, card, points)
      case GameOver(player1Name, player1Points, player2Name, player2Points) =>
        showGameOverWindow(player1Name, player1Points, player2Name, player2Points)
      case UpdateGrid(grid) =>
        showGrid()
      case UndoEvent(_) => showGrid()
        updateDisplay()
      case RedoEvent(_) => showGrid()
        updateDisplay()
      case UpdatePlayer(player1) =>
        updateStatus(s"$player1's turn.")
      case ShowCardsForPlayer(cards) =>
        showCardsGUI(cards)
      // updateDisplay()
      case PromptForPlayerName =>
        promptForPlayerName { (player1Name, player2Name) =>
          gameController.promptForPlayerName(player1Name, player2Name)
        }
      case AskForGameMode =>
        start()
      case _ => println("Invalid event.")
    }
  }

  def start(): Unit = {
    GuiInitializer.ensureInitialized()
    //  playBackgroundMusic()

    showAskForGameModeWindow { gameMode =>
      gameController.setGameMode(gameMode)
    }
  }

  private def showStartOrLoadGameWindow(onComplete: String => Unit): Unit = {
    Platform.runLater {
      val dialog = new Stage {
        title = "Start or Load Game"
        scene = new Scene(500, 400) {
          val startGameButton: Button = new Button {
            text = "Start Game"
            minWidth = 500
            minHeight = 55
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/Submit.png');" +
              "-fx-background-color: transparent; -fx-background-size: cover;" +
              "-fx-background-repeat: no-repeat;" +
              "-fx-background-insets: 0;" +
              "-fx-padding: 0;"
            font = bubblegumSans
            onAction = _ => {
              // close()
              new Thread(() => {
                onComplete("start")
              }).start()
            }
          }

          val loadGameButton: Button = new Button {
            text = "Load Game"
            minWidth = 500
            minHeight = 55
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/Submit.png');" +
              "-fx-background-color: transparent; -fx-background-size: cover;" +
              "-fx-background-repeat: no-repeat;" +
              "-fx-background-insets: 0;" +
              "-fx-padding: 0;"
            font = bubblegumSans
            onAction = _ => {
              // close()
              new Thread(() => {
                onComplete("load")
              }).start()
            }
          }

          root = new VBox(20) {
            padding = Insets(20)
            alignment = Pos.Center
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/NameBackground.png'); " +
              "-fx-background-size: cover; " +
              "-fx-background-position: center; " +
              "-fx-background-repeat: no-repeat;"
            children = Seq(
              new Label("Do you want to start a new game or load an existing one?") {
                font = bubblegumSans
                style = "-fx-font-size: 22px; -fx-text-fill: black; -fx-font-family: 'Bubblegum Sans';"
              },
              startGameButton,
              loadGameButton
            )
          }
        }
        initModality(Modality.ApplicationModal)
        resizable = false
        onCloseRequest = _ =>
          System.exit(0)
          Platform.exit()
      }
      dialog.showAndWait()
    }
  }

  private def showAskForGameModeWindow(onComplete: String => Unit): Unit = {
    Platform.runLater {
      val dialog = new Stage {
        title = "Select Game Mode"
        scene = new Scene(500, 400) {
          val singlePlayerButton: Button = new Button {
            text = "Single Player"
            minWidth = 500
            minHeight = 55
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/Submit.png');" +
              "-fx-background-color: transparent; -fx-background-size: cover;" +
              "-fx-background-repeat: no-repeat;" +
              "-fx-background-insets: 0;" +
              "-fx-padding: 0;"
            font = bubblegumSans
            onAction = _ => {
              new Thread(() => {
                onComplete("s")
                close()
              }).start()
            }
          }

          val multiPlayerButton: Button = new Button {
            text = "Multi Player"
            minWidth = 500
            minHeight = 55
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/Submit.png');" +
              "-fx-background-color: transparent; -fx-background-size: cover;" +
              "-fx-background-repeat: no-repeat;" +
              "-fx-background-insets: 0;" +
              "-fx-padding: 0;"
            font = bubblegumSans
            onAction = _ => {
              // close()
              new Thread(() => {
                onComplete("m")
                close()
              }).start()
            }
          }

          root = new VBox(20) {
            padding = Insets(20)
            alignment = Pos.Center
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/NameBackground.png'); " +
              "-fx-background-size: cover; " +
              "-fx-background-position: center; " +
              "-fx-background-repeat: no-repeat;"
            children = Seq(
              new Label("Choose Game Mode") {
                font = bubblegumSans
                style = "-fx-font-size: 22px; -fx-text-fill: black; -fx-font-family: 'Bubblegum Sans';"
              },
              singlePlayerButton,
              multiPlayerButton
            )
          }
        }
        initModality(Modality.ApplicationModal)
        resizable = false
        onCloseRequest = _ =>
          System.exit(0)
          Platform.exit()
      }
      dialog.showAndWait()
    }
  }

  def promptForPlayerName(onComplete: (String, String) => Unit): Unit = {
    Platform.runLater {
      val dialog = new Stage {
        title = "Player Names"
        scene = new Scene(500, 400) {
          val player1Field: TextField = new TextField {
            promptText = "Player 1 Name"
            font = bubblegumSans
            style = "-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;"
            prefWidth = 300
            prefHeight = 50
            alignment = Pos.Center
          }

          val player2Field: TextField = new TextField {
            promptText = "Player 2 Name"
            font = bubblegumSans
            style = "-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16px;"
            prefWidth = 300
            prefHeight = 50
            alignment = Pos.Center
          }

          val submitButton: Button = new Button {
            text = "Start Game"
            minWidth = 500
            minHeight = 55
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/Submit.png');" +
              "-fx-background-color: transparent; -fx-background-size: cover;" +
              "-fx-background-repeat: no-repeat;" +
              "-fx-background-insets: 0;" +
              "-fx-padding: 0;"
            font = bubblegumSans
            onAction = _ => {
              new Thread(() => {
                if (player1Field.text.value.nonEmpty && player2Field.text.value.nonEmpty) {
                  onComplete(player1Field.text.value, player2Field.text.value)
                  close()
                }
              }).start()
            }
          }

          val player1FieldWithImage: StackPane = createFieldWithImage(player1Field)
          val player2FieldWithImage: StackPane = createFieldWithImage(player2Field)

          root = new VBox(20) {
            padding = Insets(20)
            alignment = Pos.Center
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/NameBackground.png'); " +
              "-fx-background-size: cover; " +
              "-fx-background-position: center; " +
              "-fx-background-repeat: no-repeat;"
            children = Seq(
              new Label("Enter Player Names") {
                font = bubblegumSans
                style = "-fx-font-size: 22px; -fx-text-fill: black; -fx-font-family: 'Bubblegum Sans';" // Ensure no conflicts
              },
              player1FieldWithImage,
              player2FieldWithImage,
              submitButton
            )
          }
        }
        initModality(Modality.ApplicationModal)
        resizable = false
        onCloseRequest = _ =>
          System.exit(0)
          Platform.exit()
      }
      nameDialogStage = Some(dialog)
      dialog.showAndWait()
    }
  }

  private def updateGridDisplay(gridColors: List[(Int, Int, Option[CardInterface], Suit)]): Unit = {
    Platform.runLater {
      if (gridPane != null) {
        val newGrid = createGridFromColors(gridColors)
        gridPane.children.clear()
        gridPane.children.addAll(newGrid.children)
      }
    }
  }

  private def createGridFromColors(colors: List[(Int, Int, Option[CardInterface], Suit)]): GridPane = {
    val grid = new GridPane {
      hgap = 6
      vgap = 6
      padding = Insets(60, 0, 10, 0)
      alignment = Pos.Center
    }

    val colorMap = Map(
      "Purple" -> "#9966cc",
      "Brown" -> "#cc8566",
      "Red" -> "#d95959",
      "Blue" -> "#66b4cc",
      "Green" -> "#6ecc66",
      "White" -> "#ffffff"
    )

    for ((x, y, card, colorName) <- colors) {
      val buttonText = card.map(_.toString).getOrElse(s"($x, $y)")
      val hexColor = colorMap.getOrElse(colorName.toString, "#ffffff")

      val button = new Button(buttonText) {
        style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5;"
        prefWidth = 91
        prefHeight = 89
        onAction = _ => handleGridClick(x, y)
        onMouseEntered = _ => {
          if (selectedCardIndex.isDefined) {
            style = s"-fx-background-color: $hexColor; -fx-opacity: 0.8;"
          }
        }
        onMouseExited = _ => {
          style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5;"
        }
      }
      grid.add(button, x, y)
    }
    grid
  }


  private def updatePlayerDisplay(player1: Player, player2: Player): Unit = {
    Platform.runLater {
      updateStatus(s"${player1.name} vs ${player2.name}")
    }
  }

  private def updateCurrentPlayerStatus(player: Player): Unit = {
    Platform.runLater {
      updateStatus(s"${player.name}'s turn")
    }
  }


  private def createFieldWithImage(textField: TextField): StackPane = {
    new StackPane {
      children = Seq(
        new ImageView(new Image("file:src/main/resources/assets/backgrounds/TextField.png")) {
          fitWidth = 350
          fitHeight = 50
        },
        textField
      )
      alignment = Pos.Center
      prefWidth = 350
      prefHeight = 50
    }
  }

  private def closeNameDialog(): Unit = {
    Platform.runLater {
      nameDialogStage.foreach { dialog =>
        dialog.close()
      }
      nameDialogStage = None
    }
  }

  private def playBackgroundMusic(): Unit = {
    val musicFile = getClass.getResource("/assets/music/backgroundmusic.mp3")
    Platform.runLater {
      try {
        val media = new Media(musicFile.toURI.toString)
        val mediaPlayer = new MediaPlayer(media)
        mediaPlayer.setCycleCount(MediaPlayer.Indefinite)
        mediaPlayer.play()
      } catch {
        case e: Exception => println(s"Fehler beim Abspielen der Musik: ${e.getMessage}")
      }
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
        padding = Insets(27, 0, 0, 0)
      }

      controlPane = new HBox {
        spacing = 10
        padding = Insets(10)
        alignment = Pos.Center
        translateX = -80
        children = Seq(
          new Button() {
            translateX = 20
            translateY = -20
            style = "-fx-background-color: transparent;"
            prefWidth = 50
            prefHeight = 40
            onAction = _ => gameController.handleCommand("undo")
          },
          new Button() {
            translateX = 30
            translateY = -20
            style = "-fx-background-color: transparent;"
            prefWidth = 50
            prefHeight = 40
            onAction = _ => gameController.handleCommand("redo")
          },
          new Button() {
            translateX = 28
            translateY = -20
            prefWidth = 90
            prefHeight = 50
            style = "-fx-background-color: transparent;"
            onAction = _ => gameController.handleCommand("draw")
          }
        )
      }

      cardPane = new HBox {
        spacing = 10
        alignment = Pos.BottomCenter
      }

      gridPane = createGrid()

      val gifPath = getClass.getResource("/assets/backgrounds/ZayneChillingGif.gif")
      if (gifPath == null) {
        throw new IllegalStateException("GIF file not found in resources")
      }

      val imageView = new ImageView(new Image(gifPath.toExternalForm)) {
        fitWidth = 150
        fitHeight = 150
        preserveRatio = true
        mouseTransparent = true
      }

      val gifContainer = new StackPane {
        children = imageView
        alignment = Pos.TopRight
        managed = false
        translateX = 430
        translateY = 405
      }

      val gameContainer = new VBox(20) {
        alignment = Pos.TopCenter
        children = Seq(
          statusLabel,
          gridPane,
          controlPane
        )
      }

      val overlayPane = new StackPane {
        children = Seq(
          gameContainer,
          gifContainer
        )
        StackPane.setAlignment(gifContainer, Pos.TopRight)
      }

      val rootPane = new VBox {
        spacing = 20
        padding = Insets(10, 0, 0, 0)
        alignment = Pos.TopCenter
        children = Seq(
          overlayPane,
          cardPane
        )
        style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/Grid.png'); " +
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
        resizable = false
        onCloseRequest = _ =>
          System.exit(0)
          Platform.exit()
      }
      currentStage.show()
    }
  }


  def createGrid(): GridPane = {
    val grid = new GridPane {
      hgap = 6
      vgap = 6
      padding = Insets(60, 0, 10, 0)
      alignment = Pos.Center
    }

    val colorMap = Map(
      "Purple" -> "#9966cc",
      "Brown" -> "#cc8566",
      "Red" -> "#d95959",
      "Blue" -> "#66b4cc",
      "Green" -> "#6ecc66",
      "White" -> "#ffffff"
    )

    val colors = gameController.getGridColors

    for ((x, y, card, colorName) <- colors) {
      val buttonText = card.map(_.toString).getOrElse(s"($x, $y)")
      val hexColor = colorMap.get(colorName.toString
      ) match {
        case Some(hex) => hex
        case None => colorName
      }


      val button = new Button(buttonText) {
        style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5;"
        prefWidth = 91
        prefHeight = 89
        onAction = * => handleGridClick(x, y)
        onMouseEntered = * => {
          if (selectedCardIndex.isDefined) {
            style = s"-fx-background-color: $hexColor; -fx-opacity: 0.8;"
          }
        }
        onMouseExited = _ => {
          style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5;"
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

  private def updateStatus(message: String): Unit = {
    Platform.runLater {
      if (statusLabel != null) {
        statusLabel.text = message
      }
    }
  }

  private def updateButtonStyles(): Unit = {
    if (cardPane != null) {
      cardPane.children.foreach {
        case stackPane: javafx.scene.layout.StackPane =>
          stackPane.setStyle("")
        case button: javafx.scene.control.Button =>
          button.setStyle("")
        case _ =>
      }
    }
  }

  private def showCardsGUI(cards: Seq[CardInterface]): Unit = {
    Platform.runLater {
      try {
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
        }
        val cardButtons = cardImages.zipWithIndex.map { case (cardImage, index) =>
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
          e.printStackTrace()
      }
    }
  }

  private def showGrid(): Unit = {
    Platform.runLater {
      if (gridPane != null) {
        val newGrid = createGrid()
        gridPane.children.clear()
        gridPane.children.addAll(newGrid.children)
      }
      updateDisplay()
    }
  }

  private def updateDisplay(): Unit = {
    if (currentStage == null) {
      initialize()
    }
  }

  private def invalidPlacements(): Unit = {
    updateStatus("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
  }

  private def CardPlacementSuccesss(x: Int, y: Int, card: String, points: Int): Unit = {
    updateStatus(s"Card placed at ($x, $y): $card. Points earned: $points.")
  }

  private def PlayerTurs(playerName: String): Unit = {
    updateStatus(s"$playerName's turn.")
  }

  private def CardDrawns(playerName: String, card: String): Unit = {
    updateStatus(s"$playerName drew: $card")
  }

  private def showGameOverWindow(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int): Unit = {
    Platform.runLater {
      val winner = if (player1Points > player2Points) {
        s"$player1Name wins!"
      } else if (player2Points > player1Points) {
        s"$player2Name wins!"
      } else {
        "It's a tie!"
      }

      val dialog = new Stage {
        title = "Game Over"
        scene = new Scene(469, 669) {
          root = new VBox {
            spacing = 10
            padding = Insets(130, 0, 0, 0)
            alignment = Pos.TopCenter
            style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/GameOverBackground.png'); " +
              "-fx-background-size: cover; " +
              "-fx-background-position: center;"
            children = Seq(
              new Text("Game over!") {
                font = bubblegumSans
                style = "-fx-font-size: 24px; -fx-fill: red;"
              },
              new Text(s"$player1Name's final score: $player1Points") {
                font = bubblegumSans
                style = "-fx-font-size: 24px;"
              },
              new Text(s"$player2Name's final score: $player2Points") {
                font = bubblegumSans
                style = "-fx-font-size: 24px;"
              },
              new Text(winner) {
                font = bubblegumSans
                style = "-fx-font-size: 24px;"
              }
            )
          }
        }
        initModality(Modality.ApplicationModal)
        resizable = false
        onCloseRequest = _ =>
          System.exit(0)
          Platform.exit()
      }
      dialog.showAndWait()
    }
  }
}
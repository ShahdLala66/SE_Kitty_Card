package aview.gui

import controller.GameControllerInterface
import model.gameModelComp.CardInterface
import model.gameModelComp.baseImp.Suit.Suit
import model.gameModelComp.baseImp.{NumberCards, Player, Value}
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, TextField, ToggleButton}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.*
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.text.{Font, Text}
import scalafx.scene.{Node, Scene}
import scalafx.stage.{Modality, Stage}
import util.*

import scala.compiletime.uninitialized


class Gui(gameController: GameControllerInterface) extends Observer {
  gameController.add(this)

  private val bubblegumSans: Font = Font.loadFont(getClass.getResourceAsStream("/assets/fonds/BubblegumSans-Regular.ttf"), 20)
  private var currentStage: Stage = uninitialized
  private var cardPane: HBox = uninitialized
  private var gridPane: GridPane = uninitialized
  private var statusLabel: Label = uninitialized
  private var controlPane: HBox = uninitialized
  private var selectedCardIndex: Option[Int] = None
  private var nameDialogStage: Option[Stage] = None
  private var loadGameDialog: Option[Stage] = None
  private var multiGameDialog: Option[Stage] = None
  private var gifOverlayPane: Pane = uninitialized
  private var mediaPlayer: MediaPlayer = uninitialized


  override def update(event: GameEvent): Unit = {
    event match {
      case AskForLoadGame =>
        closeMultiDialog()
        showStartOrLoadGameWindow { choice =>
          gameController.handleCommand(choice)
        }
      case UpdateLoadedGame(gridColors, currentPlayer, p1, p2, hand) =>
        closeLoadDialog()
        updateDisplay()
        updateGridDisplay(gridColors)
        updatePlayerDisplay(p1, p2)
        updateCurrentPlayerStatus(currentPlayer)
        showCardsGUI(hand)
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
        addCatGifInCell(x, y)
        CardPlacementSuccesss(x, y, card, points)
      case GameOver(player1Name, player1Points, player2Name, player2Points) =>
        showGameOverWindow(player1Name, player1Points, player2Name, player2Points)
      case UpdateGrid(grid) =>
        showGrid()
      case UndoEvent(_) => showGrid()
        removeLastGif()
        updateDisplay()
      case RedoEvent(_) => showGrid()
        restoreLastGif()
        updateDisplay()
      case UpdatePlayer(player1) =>
        var points = gameController.getCurrentPlayerPoints
        updateStatus(s"$player1's turn. $points points.")
      case ShowCardsForPlayer(cards) =>
        showCardsGUI(cards)
      case PromptForPlayerName =>
        closeLoadDialog()
        promptForPlayerName { (player1Name, player2Name) =>
          gameController.promptForPlayerName(player1Name, player2Name)
        }
      case AskForGameMode =>
        start()
      case FreezeEnemy =>
        updateStatus("Enemy's turn is frozen.")
        updateDisplay()
      case GameSaved =>
        updateStatus("Game saved successfully.")
      case InitializeGUIForLoad =>
      case _ => println("Invalid event.")
    }
  }

  def start(): Unit = {
    GuiInitializer.ensureInitialized()
    playBackgroundMusic()

    showAskForGameModeWindow { gameMode =>
      gameController.setGameMode(gameMode)
    }
  }



  // INTRO START ____________________---

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
                // close()
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
                //  close()
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

      multiGameDialog = Some(dialog)
      dialog.showAndWait()
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
      loadGameDialog = Some(dialog)
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


  // GAME START GUI MAKE SURE ITS WOKRING

  private def createMenuBar(): HBox = {
    val saveButton = new Button("Save") {
      style = "-fx-background-color: transparent;"
      font = Font.font("Bubblegum Sans", 14)
      prefWidth = 70
      prefHeight = 30

      onAction = _ => gameController.handleCommand("save")
    }

    val musicOnImage = new Image("file:src/main/resources/assets/icons/music_on.png")
    val musicOffImage = new Image("file:src/main/resources/assets/icons/music_off.png")

    val musicToggleButton = new ToggleButton {
      graphic = new ImageView(musicOnImage) {
        fitWidth = 50
        fitHeight = 20
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"
      font = Font.font("Bubblegum Sans", 14)
      selected = true
      onAction = _ => {
        toggleBackgroundMusic()
        if (selected.value) {
          graphic = new ImageView(musicOnImage) {
            fitWidth = 50
            fitHeight = 20
            preserveRatio = true
          }
        } else {
          graphic = new ImageView(musicOffImage) {
            fitWidth = 50
            fitHeight = 20
            preserveRatio = true
          }
        }
      }
    }

    new HBox {
      spacing = 15
      padding = Insets(-8, 0, 0, 20)
      children = Seq(saveButton, musicToggleButton) // Removed the spacer
    }
  }

  import scalafx.Includes.*

  def initialize(): Unit = {
    Platform.runLater {
      statusLabel = new Label {
        text = "Welcome to the game!"
        font = bubblegumSans
        style = "-fx-font-size: 22px; -fx-text-fill: black; -fx-font-family: 'Bubblegum Sans';"
        padding = Insets(-20, 0, 0, 0)
      }

      gifOverlayPane = new Pane {
        mouseTransparent = true
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
        private var isMeowMeow: Boolean = true
        onMouseClicked = (event: MouseEvent) => {
          if (isMeowMeow) {
            updateStatus("meow meow")
          } else {
            updateStatus("Don't touch me! Go play!")
          }
          isMeowMeow = !isMeowMeow
        }
        // mouseTransparent = true
      }

      val gifContainer = new StackPane {
        children = imageView
        alignment = Pos.TopRight
        managed = false
        translateX = 430
        translateY = 358
      }

      val gameContainer = new StackPane {
        children = Seq(
          new VBox(20) {
            alignment = Pos.TopCenter
            children = Seq(
              statusLabel,
              gridPane,
              controlPane
            )
          },
          gifOverlayPane
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
          createMenuBar(), // Add the menu bar here
          overlayPane,
          cardPane
        )
        style = "-fx-background-image: url('file:src/main/resources/assets/backgrounds/GridNew.png'); " +
          "-fx-background-size: 444px 695px; " +
          "-fx-background-position: center;"
      }

      currentStage = new Stage {
        title = "Kitty Cardsss"
        scene = new Scene {
          root = rootPane
        }
        width = 444
        height = 735
        resizable = false
        onCloseRequest = _ =>
          System.exit(0)
          Platform.exit()
      }
      currentStage.show()
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
        prefHeight = 86
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



  // GUI ELEMENTS ________________________

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

  def createGrid(): GridPane = {
    val grid = new GridPane {
      hgap = 6
      vgap = 6
      padding = Insets(57, 0, 6, 0)
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
      //set the text to the top of the buttonz
      val hexColor = colorMap.get(colorName.toString
      ) match {
        case Some(hex) => hex
        case None => colorName
      }


      val button = new Button(buttonText) {
        style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5; -fx-font-size: 10px; -fx-padding: 50 0 0 0;"
        prefWidth = 91
        prefHeight = 89
        onAction = * => handleGridClick(x, y)
        onMouseEntered = * => {
          if (selectedCardIndex.isDefined) {
            style = s"-fx-background-color: $hexColor; -fx-opacity: 0.8; -fx-font-size: 13px; -fx-padding: 40 0 0 0;"
          }
        }
        onMouseExited = _ => {
          style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5; -fx-font-size: 10px; -fx-padding: 50 0 0 0;"
        }
        style = s"-fx-background-color: $hexColor; -fx-opacity: 0.5; -fx-font-size: 10px; -fx-padding: 50 0 0 0;"

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

  private val germanToEnglishColors: Map[String, String] = Map(
    "Rot" -> "Red",
    "Lila" -> "Purple",
    "Braun" -> "Brown",
    "Blau" -> "Blue",
    "Grün" -> "Green"
  )

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
              case Value.Seven => "7"
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
            if (cardImage.value.equals("7")) {
              updateStatus("Bomb card selected. The Color is a secret.")
            } else {
              val englishColor = germanToEnglishColors.getOrElse(cardImage.suit, cardImage.suit)
              s"Selected: ${cardImage.value} of $englishColor"
              updateStatus(s"${cardImage.value} of $englishColor")
            }
           // updateStatus(s"Selected: ${cardImage.value} of ${cardImage.suit}")
          }) {
            onMouseEntered = _ => {
              style =
                """
                          -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 4);
                          -fx-scale-x: 1.05;
                          -fx-scale-y: 1.05;
                        """
              translateY = -5 // Slight lift effect
            }

            onMouseExited = _ => {
              style =
                """
                          -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);
                          -fx-scale-x: 1.0;
                          -fx-scale-y: 1.0;
                        """
              translateY = 0
            }
          }
        }



        if (cardPane != null) {
          cardPane.children = cardButtons //
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
        //  gifOverlayPane.getChildren.clear()

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
    updateStatus("Kitty destroyed your card! Stop bothering it! Turn forfeited.")
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

  // Add this at the beginning of your class with other private vars
  private var gifHistory: List[Node] = List()
  private var undoneGifs: List[Node] = List() // New list for undone gifs

  private def addCatGifInCell(x: Int, y: Int): Unit = {
    Platform.runLater {
      val currentPlayer = gameController.getCurrentPlayerString
      val player1 = gameController.getPlayer1

      val gifPath = if (player1 != currentPlayer) {
        getClass.getResource("/assets/backgrounds/ZayneGrid1.gif")
      } else {
        getClass.getResource("/assets/backgrounds/XavierChillingGif.gif")
      }

      if (gifPath != null) {
        val imageView = new ImageView(new Image(gifPath.toExternalForm)) {
          fitWidth = 80
          fitHeight = 80
          preserveRatio = true
          mouseTransparent = true
        }

        val gifContainer = new StackPane {
          children = imageView
          prefWidth = 91
          prefHeight = 89
          alignment = Pos.Center
          mouseTransparent = true
        }

        // Adjusted positioning calculation
        val cellWidth = 91
        val cellHeight = 89
        val hgap = 6
        val vgap = 6
        val gridLeftOffset = 55 // Reduced from 78
        val gridTopOffset = 115 // Increased from 93

        gifContainer.layoutX = gridLeftOffset + x * (cellWidth + hgap) + (cellWidth - 40) / 2 - 8 //- to the right
        gifContainer.layoutY = gridTopOffset + y * (cellHeight + vgap) + (cellHeight - 40) / 2 - 60

        gifOverlayPane.getChildren.add(gifContainer)
        gifHistory = gifContainer :: gifHistory
        undoneGifs = List()
      }
    }
  }


  private def removeLastGif(): Unit = {
    Platform.runLater {
      gifHistory match {
        case lastGif :: remaining =>
          gifOverlayPane.getChildren.remove(lastGif)
          gifHistory = remaining
          undoneGifs = lastGif :: undoneGifs
        case Nil =>
      }
    }
  }

  private def restoreLastGif(): Unit = {
    Platform.runLater {
      undoneGifs match {
        case lastUndone :: remaining =>
          gifOverlayPane.getChildren.add(lastUndone)
          undoneGifs = remaining
          gifHistory = lastUndone :: gifHistory
        case Nil =>
      }
    }
  }

  // PROCESS INPUT ________________________  AND FUNCTIONALITY __________

  private def closeNameDialog(): Unit = {
    Platform.runLater {
      nameDialogStage.foreach { dialog =>
        dialog.close()
      }
      nameDialogStage = None
    }
  }

  private def closeLoadDialog(): Unit = {
    Platform.runLater {
      loadGameDialog.foreach { dialog =>
        dialog.close()
      }
      loadGameDialog = None
    }
  }

  private def closeMultiDialog(): Unit = {
    Platform.runLater {
      multiGameDialog.foreach { dialog =>
        dialog.close()
      }
      multiGameDialog = None
    }
  }

  private def toggleBackgroundMusic(): Unit = {
    if (mediaPlayer != null) {
      mediaPlayer.status() match {
        case MediaPlayer.Status.Playing => mediaPlayer.pause()
        case MediaPlayer.Status.Paused => mediaPlayer.play()
        case _ =>
      }
    }
  }

  private def playBackgroundMusic(): Unit = {
    val musicFile = getClass.getResource("/assets/music/backgroundmusic.mp3")
    Platform.runLater {
      try {
        val media = new Media(musicFile.toURI.toString)
        mediaPlayer = new MediaPlayer(media)
        mediaPlayer.setCycleCount(MediaPlayer.Indefinite)
        mediaPlayer.play()
      } catch {
        case e: Exception => println(s"Error playing music: ${e.getMessage}")
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

  // GAME OVER -------------------------------------------

  private def showGameOverWindow(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int): Unit = {
    Platform.runLater {
      if (currentStage != null) {
        currentStage.close()
      }

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
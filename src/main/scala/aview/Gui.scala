package aview

import controller.GameController
import scalafx.application.{JFXApp3, Platform}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.*
import scalafx.scene.text.Font
import scalafx.stage.Modality
import util.*

class Gui(gameController: GameController) extends JFXApp3 with Observer {
  private var currentPlayerCards: List[String] = List.empty
  private var currentPlayerName: String = ""
  private var currentPlayerLabel: Label = _
  private var playerHandBox: HBox = _
  private var gameGrid: GridPane = _

  def showPlayerNameDialog(): (String, String) = {
    val dialog = new Dialog[(String, String)]() {
      initModality(Modality.ApplicationModal)
      title = "Player Names"
      headerText = "Enter player names"
    }

    val player1Field = new TextField()
    val player2Field = new TextField()

    val dialogContent = new GridPane {
      hgap = 10
      vgap = 10
      padding = Insets(20)
      add(new Label("Player 1:"), 0, 0)
      add(player1Field, 1, 0)
      add(new Label("Player 2:"), 0, 1)
      add(player2Field, 1, 1)
    }
    import scalafx.Includes.jfxDialogPane2sfx


    dialog.dialogPane().content = dialogContent
    dialog.dialogPane().buttonTypes = Seq(ButtonType.OK)

    dialog.resultConverter = {
      case ButtonType.OK => (player1Field.text(), player2Field.text())
      case _ => null
    }

    val result = dialog.showAndWait()
    result match {
      case Some(value: (String, String)) => value
      case None => ("", "")
    }
  }

  private def createCardImageView(cardPath: String): ImageView = {
    try {
      new ImageView {
        image = new Image(getClass.getResourceAsStream(s"/Assets/Cards/1/$cardPath"))
        fitWidth = 100
        fitHeight = 150
        preserveRatio = true
      }
    } catch {
      case e: Exception =>
        println(s"Failed to load image: $cardPath")
        e.printStackTrace()
        new ImageView {
          fitWidth = 100
          fitHeight = 150
          style = "-fx-background-color: gray;"
        }
    }
  }

  private def initializeUI(): Unit = {
    // Initialize the grid
    gameGrid = new GridPane {
      padding = Insets(10)
      hgap = 10
      vgap = 10
      alignment = Pos.Center

      for {
        i <- 0 until 3
        j <- 0 until 3
      } {
        val cell = new StackPane {
          style = "-fx-border-color: gray; -fx-border-width: 1;"
          prefWidth = 120
          prefHeight = 170
        }
        add(cell, j, i)
      }
    }

    // Initialize player hand display
    playerHandBox = new HBox {
      spacing = 10
      padding = Insets(10)
      alignment = Pos.Center
    }

    // Initialize current player label
    currentPlayerLabel = new Label {
      text = "Current Player: "
      font = Font(16)
    }

    val inputField = new TextField {
      promptText = "Enter card index and position (e.g., '1 2 3')"
      prefWidth = 250
    }

    val submitButton = new Button("Play Card") {
      onAction = _ => {
        val input = inputField.text().trim.split(" ")
        if (input.length == 3) {
          try {
            val cardIndex = input(0).toInt
            val x = input(1).toInt - 1
            val y = input(2).toInt - 1
            gameController.handleCardPlacement(cardIndex, x, y)
            inputField.clear()
          } catch {
            case _: NumberFormatException =>
              new Alert(Alert.AlertType.Error) {
                contentText = "Invalid input format. Please use numbers only."
              }.showAndWait()
          }
        }
      }
    }

    val drawButton = new Button("Draw Card") {
      onAction = _ => {
        gameController.handleCommand("draw")
      }
    }

    val controlBox = new VBox {
      spacing = 10
      padding = Insets(10)
      alignment = Pos.Center
      children = Seq(
        currentPlayerLabel,
        new HBox {
          spacing = 10
          alignment = Pos.Center
          children = Seq(inputField, submitButton, drawButton)
        }
      )
    }

    stage = new JFXApp3.PrimaryStage {
      title = "Card Game"
      scene = new Scene(800, 600) {
        root = new VBox {
          spacing = 20
          padding = Insets(20)
          children = Seq(
            new MenuBar {
              menus = Seq(
                new Menu("Game") {
                  items = Seq(
                    new MenuItem("New Game") {
                      onAction = _ => gameController.startGame()
                    },
                    new MenuItem("Exit") {
                      onAction = _ => Platform.exit()
                    }
                  )
                },
                new Menu("Edit") {
                  items = Seq(
                    new MenuItem("Undo") {
                      onAction = _ => gameController.handleCommand("undo")
                    },
                    new MenuItem("Redo") {
                      onAction = _ => gameController.handleCommand("redo")
                    }
                  )
                }
              )
            },
            gameGrid,
            playerHandBox,
            controlBox
          )
        }
      }
    }
  }
  
  private def printGridColors(): Unit = {
    for {
      i <- 0 until 3
      j <- 0 until 3
    } {
      val color = gameController.getGridColor(i, j)
      val cell = gameGrid.getChildren.get(i * 3 + j).asInstanceOf[StackPane]
      cell.style = s"-fx-background-color: $color;"
    }
  }

  override def start(): Unit = {
    val (player1Name, player2Name) = showPlayerNameDialog()
    gameController.promptForPlayerName(player1Name, player2Name)
    initializeUI()
    println("GUI initialized, starting game...")
    // Add debug print statements
    println(s"Player 1: $player1Name, Player 2: $player2Name")
  }

    override def update(event: GameEvent): Unit = {
    println(s"Received event: $event") // Debug print
    Platform.runLater {
      event match {
        case UpdatePlayers(player1, player2) =>
          println(s"Players updated: $player1, $player2")

        case PlayerTurn(playerName) =>
          println(s"PlayerTurn event: $playerName")
          currentPlayerName = playerName
          currentPlayerLabel.text = s"Current Player: $playerName"

        case CardDrawn(playerName, card) =>
          println(s"CardDrawn event: $playerName drew $card")
          new Alert(Alert.AlertType.Information) {
            title = "Card Drawn"
            headerText = s"$playerName drew a card"
            contentText = s"Card: $card"
          }.showAndWait()

        case InvalidPlacement =>
          println("InvalidPlacement event")
          new Alert(Alert.AlertType.Warning) {
            title = "Invalid Placement"
            contentText = "Spot is either occupied or out of bounds. Turn forfeited."
          }.showAndWait()

        case CardPlacementSuccess(x, y, cardPath, points) =>
          println(s"CardPlacementSuccess event: ($x, $y) - $cardPath, $points points")
          val imageView = createCardImageView(cardPath)
          gameGrid.add(imageView, y, x)

        case GameOver(player1Name, player1Points, player2Name, player2Points) =>
          println("GameOver event")
          new Alert(Alert.AlertType.Information) {
            title = "Game Over"
            headerText = "Game Results"
            contentText =
              s"""
                 |$player1Name: $player1Points points
                 |$player2Name: $player2Points points
            """.stripMargin.trim
          }.showAndWait()

        case ShowCardsForPlayer(cards) =>
          println(s"ShowCardsForPlayer event: ${cards.mkString(", ")}")
          playerHandBox.children.clear()
          cards.foreach { card =>
            val imageView = createCardImageView(card.toString)
            playerHandBox.children.add(imageView)
          }

        case updateGrid(grid) =>
          println("UpdateGrid event")
          // Assuming printGridColors() reflects the grid visually in some way
          printGridColors()

        case UndoEvent(_) =>
          println("UndoEvent performed")
          new Alert(Alert.AlertType.Information) {
            title = "Undo"
            contentText = "Undo performed."
          }.showAndWait()

        case RedoEvent(_) =>
          println("RedoEvent performed")
          new Alert(Alert.AlertType.Information) {
            title = "Redo"
            contentText = "Redo performed."
          }.showAndWait()

        case PromptForPlayerName =>
          println("PromptForPlayerName event")
          showPlayerNameDialog()

        case UpdatePlayer(player1) =>
          println(s"UpdatePlayer event: $player1")
          currentPlayerLabel.text = s"Player: $player1"

        case _ =>
          println("Unhandled event")
      }
    }
  }

}
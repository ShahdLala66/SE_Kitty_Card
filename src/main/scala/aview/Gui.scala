// src/main/scala/aview/Gui.scala
package aview

import util.*
import util.observer.{CardDrawn, CardPlacementSuccess, GameEvent, GameOver, Invalid, InvalidPlacement, Observer, PlayerTurn, PromptForGameMode, PromptForPlayerName, RedoEvent, RemoveCardFromHand, SelectSinglePlayerOption, StrategySelection, TotalPoints, UndoEvent}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, Button, ChoiceDialog, Label}
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Alert.AlertType

class Gui extends JFXApp3 with Observer {

  private[aview] val colors = Map(
    "Green" -> "#00FF00",
    "Brown" -> "#A52A2A",
    "Purple" -> "#800080",
    "Blue" -> "#0000FF",
    "Red" -> "#FF0000",
    "White" -> "#FFFFFF"
  )

  private val gridSize = 3
  private val grid = Array.fill(gridSize, gridSize)(" ") // Represents the 3x3 grid

  var undoCallback: () => Unit = () => {}
  var redoCallback: () => Unit = () => {}

  def setUndoCallback(callback: () => Unit): Unit = {
    undoCallback = callback
  }

  def setRedoCallback(callback: () => Unit): Unit = {
    redoCallback = callback
  }

  override def start(): Unit = {
    welcomeMessage()
  }

  def welcomeMessage(): Unit = {
    val welcomeAlert = new Alert(AlertType.Information) {
      title = "Welcome"
      headerText = "Welcome to the Kitty Card Game!"
      contentText = "Players take turns drawing and placing cards on the grid.\nEarn points by placing cards on matching colors or white squares."
    }
    welcomeAlert.showAndWait()
    setupGameUI()
  }

  def setupGameUI(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Kitty Card Game"
      scene = new Scene {
        root = new VBox {
          alignment = Pos.Center
          spacing = 20
          padding = Insets(20)

          children = Seq(
            new GridPane {
              alignment = Pos.Center
              hgap = 10
              vgap = 10

              for (i <- 0 until gridSize; j <- 0 until gridSize) {
                val cell = new Label(grid(i)(j)) {
                  style = "-fx-border-color: black; -fx-font-size: 20px; -fx-alignment: center;"
                  minWidth = 50
                  minHeight = 50
                }
                add(cell, j, i)
              }
            },
            new HBox {
              alignment = Pos.Center
              spacing = 10
              children = Seq(
                new Button("Undo") {
                  onAction = _ => undoCallback()
                },
                new Button("Redo") {
                  onAction = _ => redoCallback()
                }
              )
            }
          )
        }
      }
    }
  }

  def updateGrid(x: Int, y: Int, value: String): Unit = {
    grid(x)(y) = value
    setupGameUI() // Refresh the UI
  }

  override def update(event: GameEvent): Unit = {
    event match {
      case PlayerTurn(playerName) => showInfo(s"$playerName's turn.")
      case CardDrawn(playerName, card) => showInfo(s"$playerName drew: $card")
      case InvalidPlacement() => showInfo("Invalid placement. Spot is either occupied or out of bounds. Turn forfeited.")
      case CardPlacementSuccess(x, y, card, points) =>
        updateGrid(x, y, card)
        showInfo(s"Card placed at ($x, $y): $card.")
      case GameOver(player1Name, player1Points, player2Name, player2Points) =>
        val winner = if (player1Points > player2Points) s"$player1Name wins!"
        else if (player2Points > player1Points) s"$player2Name wins!"
        else "It's a tie!"
        showInfo(s"Game over!\n$player1Name's final score: $player1Points\n$player2Name's final score: $player2Points\n$winner")
      case TotalPoints(player1Points, player2Points) => showInfo(s"Total points: Player 1: $player1Points, Player 2: $player2Points")
      case UndoEvent(_) => showInfo("Undo performed.")
      case RedoEvent(_) => showInfo("Redo performed.")
      case PromptForPlayerName(player) => promptPlayerName(player)
      case StrategySelection() => selectStrategy()
      case SelectSinglePlayerOption() => showInfo("Singleplayer mode selected.")
      case Invalid() => showInfo("Invalid...")
      case PromptForGameMode() => setupGameUI()
      case RemoveCardFromHand(player, card) => showInfo(s"$player removed card: $card")
    }
  }

  def showInfo(message: String): Unit = {
    val infoAlert = new Alert(AlertType.Information) {
      title = "Information"
      headerText = ""
      contentText = message
    }
    infoAlert.showAndWait()
  }

  def promptPlayerName(player: String): String = {
    showInfo(s"Enter the name for $player.") // Placeholder
    player
  }

  def selectStrategy(): Unit = {
    val strategies = Seq("Random Strategy", "Pre-Seen Deck Strategy")
    val dialog = new ChoiceDialog(defaultChoice = strategies.head, choices = strategies) {
      title = "Strategy Selection"
      headerText = "Choose a strategy for Multiplayer mode"
      contentText = "Select your strategy:"
    }
    dialog.showAndWait()
  }
}

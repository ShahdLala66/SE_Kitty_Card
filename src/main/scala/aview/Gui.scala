// src/main/scala/aview/Gui.scala

package aview

import controller.GameController
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scala.concurrent.{Future, Promise}
import util._

class Gui(gameController: GameController) extends JFXApp3 with Observer {

  //gameController.add(this)
  override def start(): Unit = {
    val textField = new TextField {
      layoutX = 253
      layoutY = 41
    }

    val gridPane = new GridPane {
      layoutX = 84
      layoutY = 83
      prefWidth = 450
      prefHeight = 450
      columnConstraints = Seq.fill(3)(new ColumnConstraints {
        hgrow = Priority.Sometimes
        prefWidth = 150
      })
      rowConstraints = Seq.fill(3)(new RowConstraints {
        vgrow = Priority.Sometimes
        prefHeight = 150
      })
      padding = Insets(10)
      hgap = 10
      vgap = 10
    }

    val submitButton = new Button("SubmitInput") {
      layoutX = 287
      layoutY = 67
      onAction = _ => {
        val input = textField.text.value.trim
        val coordinates = input.split(" ").map(_.toIntOption)

        if (coordinates.length == 2 && coordinates.forall(_.isDefined)) {
          val row = coordinates(0).get - 1
          val col = coordinates(1).get - 1

          if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            val imageView = new ImageView {
              image = new Image(getClass.getResourceAsStream("/yellow_card_1.png"))
              fitWidth = 130
              fitHeight = 130
              preserveRatio = true
            }
            GridPane.setRowIndex(imageView, row)
            GridPane.setColumnIndex(imageView, col)
            gridPane.children.add(imageView)
          } else {
            new Alert(Alert.AlertType.Warning) {
              contentText = "Coordinates must be between 1 and 3."
            }.showAndWait()
          }
        } else {
          new Alert(Alert.AlertType.Warning) {
            contentText = "Invalid input. Enter coordinates in 'x y' format."
          }.showAndWait()
        }
      }
    }

    val backgroundImage = new BackgroundImage(
      new Image(getClass.getResourceAsStream("/IMG_0807.png")),
      BackgroundRepeat.NoRepeat,
      BackgroundRepeat.NoRepeat,
      BackgroundPosition.Center,
      new BackgroundSize(0.95, 0.95, true, true, false, false)
    )

    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Grid Image Placement"
      scene = new Scene {
        root = new VBox {
          prefHeight = 600
          prefWidth = 640
          background = new Background(Array(backgroundImage))
          children = Seq(
            new MenuBar {
              VBox.setVgrow(this, Priority.Never)
              menus = Seq(
                new Menu("Edit") {
                  items = Seq(
                    new MenuItem("Undo"),
                    new MenuItem("Redo"),
                  )
                },
                new Menu("Help") {
                  items = Seq(
                    new MenuItem("About MyHelloApp")
                  )
                }
              )
            },
            new AnchorPane {
              VBox.setVgrow(this, Priority.Always)
              children = Seq(
                new Label("Top Left Label") {
                  layoutX = 32
                  layoutY = 14
                  textFill = Color.web("#9f9f9f")
                  font = Font(18)
                },
                new Label("Top Right Label") {
                  layoutX = 517
                  layoutY = 14
                  textFill = Color.web("#9f9f9f")
                  font = Font(18)
                },
                textField,
                submitButton,
                gridPane
              )
            }
          )
        }
      }
    }
  }
  

  override def update(event: GameEvent): Unit = {
    event match {
      case GameStart =>
        start()

      case UpdatePlayers(player1, player2) =>
        start()
        println(s"\nPlayer 1: $player1, Player 2: $player2")
      case PromptForPlayerName =>
        println("Prompt for player name")
      // Handle other events as needed...
    }
  }
}

package aview

import controller.GameController
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.stage.Stage
import util.{Observer, PromptForPlayerName}

import scala.concurrent.Promise

class Name(gameController: GameController) extends JFXApp3 with Observer {
  var nameSubmitted: Promise[Unit] = _


  override def start(): Unit = {
    val player1Field = new TextField {
      promptText = "Enter Player 1 Name"
    }

    val player2Field = new TextField {
      promptText = "Enter Player 2 Name"
    }

    val alertStage = new Stage {
      title = "Player Name Input"
      scene = new Scene {
        content = new VBox {
          spacing = 10
          children = Seq(
            new Label("Enter Player Names:"),
            player1Field,
            player2Field,
            new Button("Submit") {
              onAction = _ => {
                val player1Name = player1Field.text.value.trim
                val player2Name = player2Field.text.value.trim
                if (player1Name.nonEmpty && player2Name.nonEmpty) {
                  gameController.promptForPlayerName(player1Name, player2Name)
                  println(s"Player 1: $player1Name, Player 2: $player2Name")
                  //  alertStage.close()
                  nameSubmitted.success(())
                } else {
                  new Alert(Alert.AlertType.Warning) {
                    contentText = "Both player names must be entered."
                  }.showAndWait()
                }
              }
            }
          )
        }
      }
    }
    alertStage.showAndWait()
  }

  def test(): Unit = {
    println("Test")
  }

  override def update(event: util.GameEvent): Unit = {
    event match {
      case PromptForPlayerName =>
        test()
      case _ =>
        println("Name submitted")
    }
  }
}

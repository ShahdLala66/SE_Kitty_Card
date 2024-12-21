package aview

import controller.GameController
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}
import scalafx.geometry.{Insets, Pos}

class PlayerNameDialog(gameController: GameController) extends Stage {
  initModality(Modality.ApplicationModal)
  title = "Player Name Input"

  private val player1Field = new TextField {
    promptText = "Enter Player 1 Name"
  }

  private val player2Field = new TextField {
    promptText = "Enter Player 2 Name"
  }

  scene = new Scene {
    content = new VBox {
      spacing = 10
      padding = Insets(20)
      alignment = Pos.Center
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
              close()
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

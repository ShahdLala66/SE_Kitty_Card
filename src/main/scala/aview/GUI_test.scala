package aview

import scala.swing._
import scala.swing.event.ButtonClicked
import javax.swing.{ImageIcon, Timer}
import java.awt.event.ActionEvent
import java.awt.{Graphics2D, Image}

object GUI_test extends SimpleSwingApplication {
  def top: Frame = new MainFrame {
    title = "Kitty Card Game"
    preferredSize = new Dimension(800, 600)

    val player1Label = new Label {
      text = "Player 1: "
    }

    val player2Label = new Label {
      text = "Player 2: "
    }

    val player1NameField = new TextField {
      columns = 10
    }

    val player2NameField = new TextField {
      columns = 10
    }

    val startButton = new Button {
      text = "Start Game"
    }

    val drawCardButton = new Button {
      text = "Draw Card"
      enabled = false
    }

    val gameStatusLabel = new Label {
      text = "Game not started"
    }

    val gridLabels = Array.ofDim[Label](3, 3)
    val gridPanel = new GridPanel(3, 3) {
      preferredSize = new Dimension(300, 300)
      for (i <- 0 until 3; j <- 0 until 3) {
        val label = new Label {
          text = "Empty"
          border = Swing.LineBorder(java.awt.Color.BLACK)
        }
        gridLabels(i)(j) = label
        contents += label
      }
    }

    val player1Area = new BoxPanel(Orientation.Vertical) {
      contents += player1Label
      contents += player1NameField
      contents += new Label {
        text = "Player 1 Picture"
        preferredSize = new Dimension(100, 100)
        border = Swing.LineBorder(java.awt.Color.BLACK)
      }
    }

    val player2Area = new BoxPanel(Orientation.Vertical) {
      contents += player2Label
      contents += player2NameField
      contents += new Label {
        text = "Player 2 Picture"
        preferredSize = new Dimension(100, 100)
        border = Swing.LineBorder(java.awt.Color.BLACK)
      }
    }

    val catLabel = new Label {
      icon = new ImageIcon("path/to/cat/image.png")
      visible = false
    }

    contents = new BorderPanel {
      layout(player1Area) = BorderPanel.Position.West
      layout(player2Area) = BorderPanel.Position.East
      layout(gridPanel) = BorderPanel.Position.Center
      layout(new BoxPanel(Orientation.Vertical) {
        contents += startButton
        contents += drawCardButton
        contents += gameStatusLabel
        contents += catLabel
      }) = BorderPanel.Position.South
    }

    listenTo(startButton, drawCardButton)
    reactions += {
      case ButtonClicked(`startButton`) =>
        gameStatusLabel.text = "Game started"
        drawCardButton.enabled = true
        startCatAnimation()

      case ButtonClicked(`drawCardButton`) =>
        gameStatusLabel.text = "Card drawn"
        gridLabels(2)(2).icon = new ImageIcon("src/main/resources/img.png")
    }

    def startCatAnimation(): Unit = {
      val timer = new Timer(100, (_: ActionEvent) => {
        catLabel.visible = !catLabel.visible
      })
      timer.start()
    }
  }
}
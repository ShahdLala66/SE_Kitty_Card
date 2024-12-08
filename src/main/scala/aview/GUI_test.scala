package aview

import scala.swing._
import scala.swing.event.ButtonClicked

object GUI_test extends SimpleSwingApplication {
  def top: Frame = new MainFrame {
    title = "Simple Swing App"
    preferredSize = new Dimension(400, 200)

    val label = new Label {
      text = "Hello, Scala Swing!"
    }

    val button = new Button {
      text = "Click Me"
    }

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      contents += button
      border = Swing.EmptyBorder(30, 30, 10, 30)
    }

    listenTo(button)
    reactions += {
      case ButtonClicked(`button`) =>
        Dialog.showMessage(contents.head, "Button clicked!", title = "Information")
    }
  }
}
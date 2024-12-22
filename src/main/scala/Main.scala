import scalafx.application.JFXApp3
import aview.Tui
import controller.GameController

object Main extends JFXApp3 {
  override def start(): Unit = {
    val controller = new GameController()
    val tui = new Tui(controller)

    // Set up observers
    controller.setObserver(tui)

    // Start the TUI
    tui.start()
  }
}
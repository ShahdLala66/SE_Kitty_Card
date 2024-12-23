import aview.Tui
import controller.GameController
import scalafx.application.JFXApp3

object Main extends JFXApp3 {
  override def start(): Unit = {
    val controller = new GameController()
    val tui = new Tui(controller)

    // Set up observers
    controller.setObserver(tui)

    controller.startGame()
    // Start the TUI
    //tui.start()
  }
}
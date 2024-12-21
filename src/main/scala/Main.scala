import scalafx.application.JFXApp3
import aview.{Gui, Tui, Name}
import controller.GameController

object Main extends JFXApp3 {
  override def start(): Unit = {
    val controller = new GameController()
    val gui = new Gui(controller)

    // Set up observers
    controller.setObserver(gui)

    // Start the GUI (which will handle game initialization)
    gui.start()
  }
}
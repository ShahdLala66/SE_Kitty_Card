import aview.Gui.GameGuiTui
import aview.Tui
import controller.GameController

object Main extends App {
  val controller = new GameController()
  val tui = new Tui(controller)
  val gui = new GameGuiTui(controller)

  // Set up observers
  //gui.initialize()
  controller.addObserver(gui)
  controller.addObserver(tui)

  controller.startGame()
  // Start the TUI
  //tui.start()
}

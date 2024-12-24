import aview.Gui.GameGuiTui
import aview.Tui
import controller.GameController
import scalafx.application.JFXApp3

object Main extends App {
    val controller = new GameController()
   // val tui = new Tui(controller)
    val tui = new GameGuiTui(controller)

    // Set up observers
  tui.initialize()


  controller.setObserver(tui)

    controller.startGame()
    // Start the TUI
    //tui.start()
  }

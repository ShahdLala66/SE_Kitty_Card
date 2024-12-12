import controller.GameController
import aview.Tui
import aview.Gui
import scalafx.application.JFXApp3

object Main extends JFXApp3 {
  override def start(): Unit = {
    val controller = new GameController()
    val tui = new Tui()
    val gui = new Gui()

    //controller.setObserver(tui)
    controller.setObserver(gui)
   // tui.printCatLoop()
    //tui.welcomeMessage()
    controller.startGame()

    // Launch the GUI
    gui.main(Array())
  }
}
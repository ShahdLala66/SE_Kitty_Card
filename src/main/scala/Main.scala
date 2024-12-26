import aview.Gui.GameGuiTui
import aview.Tui
import com.google.inject.Guice
import controller.{GameController, GameControllerInterface}

object Main extends App {
  val injector = Guice.createInjector(new MainModule)
  val controller = injector.getInstance(classOf[GameControllerInterface])
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

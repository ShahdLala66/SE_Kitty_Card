import aview.Tui
import aview.gui.Gui
import com.google.inject.Guice
import controller.GameControllerInterface

object Main extends App {
    private val injector = Guice.createInjector(new JsonModule)
    val controller = injector.getInstance(classOf[GameControllerInterface])
    val tui = new Tui(controller)
    val gui = new Gui(controller)

    controller.startGame()
   
}
// src/main/scala/Main.scala
import aview.gui.GameGuiTui
import aview.Tui
import com.google.inject.Guice
import controller.GameControllerInterface

object Main extends App {
  private val injector = Guice.createInjector(new MainModule)
  val controller = injector.getInstance(classOf[GameControllerInterface])
  val tui = new Tui(controller)
  val gui = new GameGuiTui(controller)

  controller.startGame()
}
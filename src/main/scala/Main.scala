// src/main/scala/Main.scala
import aview.Gui.GameGuiTui
import aview.Tui
import com.google.inject.Guice
import controller.GameControllerInterface

object Main extends App {
  val injector = Guice.createInjector(new MainModule)
  val controller = injector.getInstance(classOf[GameControllerInterface])
  val tui = new Tui(controller)
  val gui = new GameGuiTui(controller)

 // val musicPlayer = new BackgroundMusicPlayer("src/main/resources/backgroundmusic.mp3")
 // musicPlayer.play()

  controller.startGame()
}
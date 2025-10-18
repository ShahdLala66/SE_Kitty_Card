package SE_Kitty_Card

import controller.baseImp.GameController
import aview.Tui

object GameLauncher {
  lazy val controller = new GameController()
  lazy val tui = new Tui(controller)
  lazy val gui = new aview.gui.Gui(controller)

  def startGame(): Unit =
    controller.startGame()

}

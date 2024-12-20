// src/main/scala/Main.scala


import aview.{Gui, Name, Tui}
import controller.GameController

object Main extends App {
  val controller = new GameController()
  val tui = new Tui(controller)
 // val gui = new Gui(controller)
  //val name = new Name(controller)


  controller.startGame()

  //gui.main(Array.empty)
  //name.main(Array.empty)

}
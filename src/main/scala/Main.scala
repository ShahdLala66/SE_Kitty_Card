// src/main/scala/Main.scala
import controller.GameController
import aview.Tui

object Main extends App {
  val controller = new GameController()
  val tui = new Tui()

  controller.setObserver(tui)
 // tui.printCatLoop()
  //tui.welcomeMessage()
  controller.startGame()
}
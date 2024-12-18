// src/main/scala/Main.scala

import controller.GameController
import aview.Tui

object Main extends App {
    val controller = new GameController()
    val tui = new Tui(controller)

    //tui.promptForPlayerName()
    controller.setObserver(tui)
    tui.start()
    controller.startGame()

    //tui.printCatLoop()
    //tui.welcomeMessage()
}
package aview.Gui

import controller.GameController
import model.cards.{Card, NumberCards, Value}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.stage.Stage
import util.*
import java.util.concurrent.CountDownLatch

// First, create a singleton object to manage JavaFX initialization
object GuiInitializer extends JFXApp3 {
  private var isInitialized = false

  def ensureInitialized(): Unit = {
    if (!isInitialized) {
      // Only initialize once
      new Thread(() => {
        try {
          println("Starting JavaFX initialization...")
          main(Array())
          isInitialized = true
          println("JavaFX initialized successfully")
        } catch {
          case e: Exception =>
            println(s"Failed to initialize JavaFX: ${e.getMessage}")
            e.printStackTrace()
        }
      }).start()

      // Give JavaFX time to initialize
      Thread.sleep(1000)
    }
  }

  override def start(): Unit = {
    // This just needs to exist to initialize JavaFX
    println("JavaFX platform started")
  }
}
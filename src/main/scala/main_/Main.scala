package main_

// import aview.Tui  // Disabled for web/WebSocket mode
// import aview.gui.Gui  // Disabled for Kubernetes deployment
import com.google.inject.Guice
import controller.GameControllerInterface

object Main extends App {
    private val injector = Guice.createInjector(new XmlModule)
    val controller = injector.getInstance(classOf[GameControllerInterface])
    
    // TUI and GUI disabled for web/Kubernetes deployment - using WebSocket instead
    // val tui = new Tui(controller)
    // val gui = new Gui(controller)

    // Don't start the game automatically - wait for WebSocket connection
    // controller.startGame()
   
}
package aview.gui

import scalafx.application.JFXApp3

object GuiInitializer extends JFXApp3 {
    private var isInitialized = false

    def ensureInitialized(): Unit = {
        if (!isInitialized) {
            new Thread(() => {
                try {
                    main(Array())
                    isInitialized = true
                } catch {
                    case e: Exception =>
                        println(s"Failed to initialize JavaFX: ${e.getMessage}")
                        e.printStackTrace()
                }
            }).start()
            Thread.sleep(1000)
        }
    }

    override def start(): Unit = {
        stage = new JFXApp3.PrimaryStage {
            title.value = " "
            width = 1
            height = 1
        }
    }
}
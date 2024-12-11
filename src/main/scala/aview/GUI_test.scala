import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.VBox
import scalafx.animation.RotateTransition
import scalafx.util.Duration

object ScalaFXExample extends JFXApp3 {

  override def start(): Unit = {
    val imageView = new ImageView(new Image("file:src/main/resources/img.png")) {
      fitWidth = 200
      fitHeight = 200
    }

    def rotateImage(): Unit = {
      //flriping the imigae from back to front
      val rotateTransition = new RotateTransition(Duration(1000), imageView) {
        byAngle = 360
      }
      rotateTransition.play()
    }

    val yourTurnButton = new Button("Rotate Zayneeee") {
      onAction = _ => rotateImage()
    }

    val rootNode = new VBox {
      spacing = 10
      children = Seq(imageView, yourTurnButton)
    }

    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Example"
      scene = new Scene {
        root = rootNode
      }
    }
  }
}
package aview

import aview.CardButtonApp.stage
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle // Import the necessary implicit conversion

class CardButton(imagePath: String, onClick: () => Unit) extends StackPane {
  // Load the image
  private val cardImage = new Image(imagePath)
  private val imageView = new ImageView(cardImage) {
    fitWidth = 100
    fitHeight = 150
  }

  // Create a rectangle to act as the card border
  private val cardBorder = new Rectangle {
    width = 100
    height = 150
    fill = Color.Transparent
    stroke = Color.Black
    strokeWidth = 2
  }

  // Add the image and border to the StackPane
  children.addAll(cardBorder, imageView)

  // Track whether the card is selected
  private var selected = false

  // Handle click events
  onMouseClicked = (event: MouseEvent) => {
    selected = !selected // Toggle selection state
    if (selected) {
      cardBorder.stroke = Color.Red // Change border color to red when selected
      cardBorder.fill = Color.Gray.opacity(0.3) // Add a semi-transparent overlay
    } else {
      cardBorder.stroke = Color.Black // Revert border color when deselected
      cardBorder.fill = Color.Transparent // Remove overlay
    }
    onClick()
  }
}

object CardButtonApp extends JFXApp3 {
  override def start(): Unit = {
    val cardButton = new CardButton("Assets/Cards/1/1-Blau.png", () => {
      println("Card clicked!")
    })

    stage = new JFXApp3.PrimaryStage {
      title = "Card Button Example"
      scene = new Scene {
        content = new VBox {
          children = cardButton
        }
      }
    }
  }
}

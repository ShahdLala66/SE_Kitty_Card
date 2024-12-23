package aview.Gui

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle



// UI Component for a Card
class CardButton(card: CardImage, onClick: CardImage => Unit) extends StackPane {
  // Load the image based on the card
  private val cardImage = new Image(card.imagePath)
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
  onMouseClicked = (_: MouseEvent) => {
    selected = !selected // Toggle selection state
    if (selected) {
      cardBorder.stroke = Color.Red // Change border color to red when selected
      cardBorder.fill = Color.Gray.opacity(0.3) // Add a semi-transparent overlay
    } else {
      cardBorder.stroke = Color.Black // Revert border color when deselected
      cardBorder.fill = Color.Transparent // Remove overlay
    }
    onClick(card) // Pass the clicked card to the handler
  }
  
    // Set the border color of the button
    def setBorderColor(color: Color): Unit = {
      style = s"-fx-border-color: ${color.toString}; -fx-border-width: 3"
    }
  }



package aview.gui

import scalafx.Includes.*
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class CardButton(card: CardImage, onClick: CardImage => Unit) extends StackPane {
    private val cardImage = new Image(card.imagePath)
    private val imageView = new ImageView(cardImage) {
        fitWidth = 100
        fitHeight = 150
    }

    private val cardBorder = new Rectangle {
        width = 100
        height = 150
        fill = Color.Transparent
        stroke = Color.Black
        strokeWidth = 2
    }

    children.addAll(cardBorder, imageView)
    private var selected = false
    onMouseClicked = (_: MouseEvent) => {
        selected = !selected
        if (selected) {
            cardBorder.stroke = Color.Red
            cardBorder.fill = Color.Gray.opacity(0.3)
        } else {
            cardBorder.stroke = Color.Black
            cardBorder.fill = Color.Transparent
        }
        onClick(card)
    }

    def setBorderColor(color: Color): Unit = {
        style = s"-fx-border-color: ${color.toString}; -fx-border-width: 3"
    }
}



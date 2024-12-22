package aview

import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import model.cards.NumberCards
import scalafx.Includes.handle

class CardButton(card: NumberCards, index: Int, onCardSelected: Int => Unit) extends Button {
  private def getImagePath(card: NumberCards): String = {
    val colorMapping = Map(
      "green" -> "Grün",
      "brown" -> "Braun",
      "purple" -> "Lila",
      "blue" -> "Blau",
      "red" -> "Rot"
    )

    val colorName = colorMapping.get(card.getColor)
    colorName match {
      case Some(name) => s"Assets/Cards/${card.value}/${card.value}-${name}.png"
      case None => "/src/main/resources/yellow_card_1.png"
    }
  }
  private val cardImage = new Image(getClass.getResourceAsStream(getImagePath(card)))
  private val imageView = new ImageView(cardImage) {
    fitWidth = 100
    fitHeight = 150
  }

  graphic = imageView
  style = "-fx-background-color: transparent;"

  onAction = handle {
    onCardSelected(index)
  }
}
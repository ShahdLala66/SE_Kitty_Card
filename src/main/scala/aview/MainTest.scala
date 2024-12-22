package aview

import aview.CardGameApp.stage
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.HBox

object CardGameApp extends JFXApp3 {
  override def start(): Unit = {
    // Example cards
    val cards = Seq(
      CardImage("1", "Blau"),
      CardImage("2", "Blau"),
      CardImage("3", "Blau"),
      CardImage("1", "Rot"),
      CardImage("2", "Rot")
    )

    // Create CardButtons for each card
    val cardButtons = cards.map(card => new CardButton(card, onClick = handleCardClick))

    stage = new JFXApp3.PrimaryStage {
      title = "Card Game Example"
      scene = new Scene {
        content = new HBox {
          spacing = 10
          children = cardButtons
        }
      }
    }
  }

  // Handle card clicks
  def handleCardClick(card: CardImage): Unit = {
    println(s"Clicked on card: ${card.value} of ${card.suit}")
  }
}

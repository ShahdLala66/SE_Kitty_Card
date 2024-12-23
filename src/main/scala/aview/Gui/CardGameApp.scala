package aview.Gui

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.stage.Stage

object CardGameApp extends JFXApp3 {
  private var selectedCard: Option[CardImage] = None

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

    // Create a 3x3 grid
    val grid = new GridPane
    for (x <- 0 until 3; y <- 0 until 3) {
      grid.add(new GridCell(x, y, handleGridClick), x, y)
    }

    stage = new JFXApp3.PrimaryStage {
      title = "Card Game Example"
      scene = new Scene {
        content = new HBox {
          spacing = 10
          children = Seq(
            new HBox {
              spacing = 10
              children = cardButtons
            },
            grid
          )
        }
      }
    }
  }

  // Handle card clicks
  def handleCardClick(card: CardImage): Unit = {
    println(s"Selected card: ${card.value} of ${card.suit}")
    selectedCard = Some(card)
  }

  // Handle grid cell clicks
  def handleGridClick(x: Int, y: Int): Unit = {
    selectedCard match {
      case Some(card) =>
        println(s"Placed card: ${card.value} of ${card.suit} at ($x, $y)")
        selectedCard = None
      case None =>
        println(s"No card selected. Clicked on cell ($x, $y)")
    }
  }
}
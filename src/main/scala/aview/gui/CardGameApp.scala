package aview.gui

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color

object CardGameApp extends JFXApp3 {
    private var selectedCard: Option[CardImage] = None
    private var usedCards: Set[CardImage] = Set.empty // Track used cards
    private var cardButtonMap: Map[CardImage, CardButton] = Map.empty // Map cards to their buttons
    private var gridCellMap: Map[(Int, Int), Option[CardImage]] = Map.empty // Track what card is placed at each grid position

    override def start(): Unit = {
        val cards = Seq(
            CardImage("1", "Blau"),
            CardImage("2", "Blau"),
            CardImage("3", "Blau"),
            CardImage("1", "Rot"),
            CardImage("2", "Rot")
        )

        val cardButtons = cards.map { card =>
            val button = new CardButton(card, onClick = handleCardClick)
            cardButtonMap += (card -> button)
            button
        }

        val grid = new GridPane
        for (x <- 0 until 3; y <- 0 until 3) {
            grid.add(new GridCell(x, y, handleGridClick), x, y)
            gridCellMap += ((x, y) -> None)
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

    private def handleCardClick(card: CardImage): Unit = {
        selectedCard match {
            case Some(prevCard) =>
                cardButtonMap.get(prevCard).foreach { button =>
                    button.setBorderColor(Color.Black)
                }
            case None =>
        }

        if (usedCards.contains(card)) {
            println(s"Card ${card.value} of ${card.suit} is already used and cannot be selected again.")
        } else {
            println(s"Selected card: ${card.value} of ${card.suit}")
            selectedCard = Some(card)
            cardButtonMap.get(card).foreach { button =>
                button.setBorderColor(Color.Red)
            }
        }
    }

    def handleGridClick(x: Int, y: Int): Unit = {
        selectedCard match {
            case Some(card) =>
                gridCellMap.get((x, y)) match {
                    case Some(None) => // Cell is empty, so we can place the card here
                        println(s"Placed card: ${card.value} of ${card.suit} at ($x, $y)")
                        gridCellMap += ((x, y) -> Some(card)) // Update the grid cell map with the placed card
                        usedCards += card // Mark the card as used
                        selectedCard = None // Deselect the card

                        // Disable the card button and change its border color
                        cardButtonMap.get(card).foreach { button =>
                            button.setBorderColor(Color.Gray) // Change the border color once placed
                            button.disable = true // Disable the card button after placement
                        }
                    case Some(Some(_)) => // Cell is already occupied, prevent placing another card
                        println(s"Cell ($x, $y) is already occupied.")
                    case None => println(s"Cell ($x, $y) is out of bounds.")
                }
            case None =>
                println(s"No card selected. Clicked on cell ($x, $y)")
        }
    }
}


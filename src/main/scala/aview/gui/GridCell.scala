package aview.gui

import scalafx.scene.control.Button

class GridCell(x: Int, y: Int, onClick: (Int, Int) => Unit) extends Button {
    text = s"($x, $y)"
    onAction = _ => onClick(x, y)

    def setCard(card: Option[CardImage]): Unit = {
        graphic = card.map { card =>
            new CardButton(card, _ => ())
        }.orNull
    }

    def getCellAt(x: Int, y: Int): Option[GridCell] = {
        if (this.x == x && this.y == y) {
            Some(this)
        } else {
            None
        }
    }


}